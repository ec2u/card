/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonLocation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.http.ResponseEntity.*;

import static java.lang.String.format;
import static java.util.stream.Collectors.*;

@Slf4j
@ControllerAdvice
@SpringBootApplication(exclude=ErrorMvcAutoConfiguration.class)
public class CardApplication {

    public static void main(final String... args) {
        SpringApplication.run(CardApplication.class, args);
    }


    //// Exception Handlers ////////////////////////////////////////////////////////////////////////////////////////////

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Object> handle(final HttpException e) {

        final HttpStatus status=e.getStatus();
        final Serializable report=e.getReport();

        if ( status.is5xxServerError() ) {

            log.error("", e);

        } else if ( e.getCause() != null ) {

            log.warn("", e);

        }

        return report == null ? status(status).build()
                : report instanceof String ? status(status).contentType(TEXT_PLAIN).body(report)
                : status(status).contentType(APPLICATION_JSON).body(report);

    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handle(final ResponseStatusException e) {

        final HttpStatus status=e.getStatus();
        final String reason=e.getReason();

        return reason == null ? status(status).headers(e.getResponseHeaders()).build()
                : status(status).contentType(TEXT_PLAIN).headers(e.getResponseHeaders()).body(reason);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handle(final MethodArgumentNotValidException e) {

        return badRequest()
                .contentType(APPLICATION_JSON)
                .body(e.getBindingResult().getAllErrors().stream().collect(toMap(

                        error -> ((FieldError)error).getField(), // !!! cast
                        error -> Optional.ofNullable(error.getDefaultMessage()).orElse("")

                )));

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handle(final HttpMessageNotReadableException e) {

        if ( e.getCause() instanceof JacksonException ) {

            return handle((JacksonException)e.getCause());

        } else {

            return badRequest()
                    .contentType(TEXT_PLAIN)
                    .body(e.getMessage());

        }

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Collection<String>> handle(final HttpRequestMethodNotSupportedException e) {

        return status(METHOD_NOT_ALLOWED).build();

    }

    @ExceptionHandler(JacksonException.class)
    public ResponseEntity<String> handle(final JacksonException e) {

        final JsonLocation location=e.getLocation();
        final String message=e.getOriginalMessage();

        return badRequest()
                .contentType(TEXT_PLAIN)
                .body(format("(%d:%d) %s", location.getLineNr(), location.getColumnNr(), message));

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handle(final ConstraintViolationException e) {

        return unprocessableEntity()
                .contentType(APPLICATION_JSON)
                .body(e.getConstraintViolations().stream().collect(groupingBy(

                        violation -> violation.getPropertyPath().toString(),
                        mapping(ConstraintViolation::getMessage, toSet())

                )));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Collection<String>> handle(final Exception e) {

        log.error("unhandled exception", e);

        return internalServerError().build();

    }


    //// Exceptions ////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    public static final class HttpException extends RuntimeException {

        private static final long serialVersionUID=3697769429806178835L;


        private final HttpStatus status;
        private final Serializable report;


        public HttpException(final HttpStatus status) {

            if ( status == null ) {
                throw new NullPointerException("null status");
            }

            this.status=status;
            this.report=null;
        }

        public HttpException(final HttpStatus status, final Serializable report) {

            if ( status == null ) {
                throw new NullPointerException("null status");
            }

            if ( report == null ) {
                throw new NullPointerException("null report");
            }

            this.status=status;
            this.report=report;
        }

        public HttpException(final HttpStatus status, final Throwable cause) {

            if ( status == null ) {
                throw new NullPointerException("null status");
            }

            if ( cause == null ) {
                throw new NullPointerException("null cause");
            }

            this.status=status;
            this.report=cause.getMessage();

            initCause(cause);

        }

    }

}
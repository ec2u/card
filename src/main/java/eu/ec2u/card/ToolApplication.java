/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.http.ResponseEntity.status;

import static java.lang.String.format;
import static java.util.stream.Collectors.*;

@Slf4j
@Configuration
@ControllerAdvice
@SpringBootApplication
public class ToolApplication implements WebMvcConfigurer {

    public static void main(final String... args) {
        SpringApplication.run(ToolApplication.class, args);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new ToolLoader());
    }


    //// Exception Handlers ////////////////////////////////////////////////////////////////////////////////////////////

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handle(final HttpMessageNotReadableException e) {

        return status(BAD_REQUEST) // 400
                .contentType(TEXT_PLAIN)
                .body(Optional.ofNullable(e.getCause())

                        .filter(JacksonException.class::isInstance)
                        .map(JacksonException.class::cast)

                        .map(p -> {


                            final JsonLocation location=p.getLocation();
                            final String message=p.getOriginalMessage();

                            return format("(%d:%d) %s", location.getLineNr(), location.getColumnNr(), message);


                        })

                        .orElseGet(e::getMessage)
                );

    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Collection<String>> handle(final NoHandlerFoundException e) {

        return status(NOT_FOUND).build(); // 404

    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Collection<String>> handle(final NoSuchElementException e) {

        return status(NOT_FOUND).build(); // 404

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Collection<String>> handle(final HttpRequestMethodNotSupportedException e) {

        return status(METHOD_NOT_ALLOWED).build(); // 405

    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handle(final IllegalStateException e) {

        return status(CONFLICT).body(e.getMessage()); // 409

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handle(final MethodArgumentNotValidException e) {

        return status(UNPROCESSABLE_ENTITY) // 422
                .contentType(APPLICATION_JSON)
                .body(e.getBindingResult().getAllErrors().stream().collect(toMap(

                        error -> ((FieldError)error).getField(), // !!! cast
                        error -> Optional.ofNullable(error.getDefaultMessage()).orElse("")

                )));

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handle(final ConstraintViolationException e) {

        return status(UNPROCESSABLE_ENTITY) // 422
                .contentType(APPLICATION_JSON)
                .body(e.getConstraintViolations().stream().collect(groupingBy(

                        violation -> violation.getPropertyPath().toString(),
                        mapping(ConstraintViolation::getMessage, toSet())

                )));

    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Collection<String>> handle(final Exception e) {

        log.error("unhandled exception", e);

        return status(INTERNAL_SERVER_ERROR).build(); // 500

    }

}
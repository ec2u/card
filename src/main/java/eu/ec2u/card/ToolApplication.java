/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonLocation;
import static java.lang.String.format;
import static java.util.stream.Collectors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.status;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Configuration
@ControllerAdvice
@SpringBootApplication
public class ToolApplication implements WebMvcConfigurer {

	public static void main(String... args) {

		SpringApplication.run(ToolApplication.class, args);

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(new ToolLoader());

	}


	//// Exception Handlers
	// //////////////////////////////////////////////////////////////////////////////////////////

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handle(HttpMessageNotReadableException exception) {

		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setMessage(Optional.ofNullable(exception.getCause())

				.filter(JacksonException.class::isInstance)
				.map(JacksonException.class::cast)
				.map(p -> {

					JsonLocation location = p.getLocation();
					String message = p.getOriginalMessage();

					return format("(%d:%d) %s", location.getLineNr(), location.getColumnNr(), message);

				})

				.orElseGet(exception::getMessage));

		return buildResponseEntity(apiError);   // 400

	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Object> handle(NoHandlerFoundException exception) {

		ApiError apiError = new ApiError(NOT_FOUND);
		apiError.setMessage(exception.getMessage());

		return buildResponseEntity(apiError);   // 404

	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Object> handle(NoSuchElementException exception) {

		ApiError apiError = new ApiError(NOT_FOUND);    // 404
		apiError.setMessage(exception.getMessage());

		return buildResponseEntity(apiError);

	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> handle(HttpRequestMethodNotSupportedException exception) {

		ApiError apiError = new ApiError(METHOD_NOT_ALLOWED);    // 405
		apiError.setMessage(exception.getMessage());

		return buildResponseEntity(apiError);

	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<Object> handle(IllegalStateException exception) {

		log.warn("conflict", exception);

		ApiError apiError = new ApiError(CONFLICT);    // 409
		apiError.setMessage(exception.getMessage());

		return buildResponseEntity(apiError);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handle(MethodArgumentNotValidException exception) {

		ResponseEntity<Object> responseEntity = status(UNPROCESSABLE_ENTITY) // 422
				.contentType(APPLICATION_JSON)
				.body(exception.getBindingResult().getAllErrors().stream().collect(toMap(

						error -> ((FieldError) error).getField(), // !!! cast
						error -> Optional.ofNullable(error.getDefaultMessage()).orElse("")

				)));

		ApiError apiError = new ApiError(responseEntity.getStatusCode());
		apiError.setMessage(exception.getLocalizedMessage());

		return buildResponseEntity(apiError);

	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handle(ConstraintViolationException exception) {

		ResponseEntity<Object> responseEntity = status(UNPROCESSABLE_ENTITY) // 422
				.contentType(APPLICATION_JSON)
				.body(exception.getConstraintViolations().stream().collect(groupingBy(

						violation -> violation.getPropertyPath().toString(),
						mapping(ConstraintViolation::getMessage, toSet())

				)));

		ApiError apiError = new ApiError(responseEntity.getStatusCode());
		apiError.setMessage(exception.getLocalizedMessage());

		return buildResponseEntity(apiError);

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handle(Exception exception) {

		log.error("unhandled exception", exception);

		ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR);    // 405
		apiError.setMessage(exception.getMessage());

		return buildResponseEntity(apiError);

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {

		return new ResponseEntity<>(apiError, apiError.getStatus());

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////

	@ExceptionHandler(WrongQueryArgumentsException.class)
	public ResponseEntity<Object> handle(WrongQueryArgumentsException exception) {

		ApiError apiError = new ApiError(BAD_REQUEST);    // 400
		apiError.setMessage(exception.getMessage());

		return buildResponseEntity(apiError);

	}

	public static final class WrongQueryArgumentsException extends IllegalArgumentException {

		public WrongQueryArgumentsException(String errorMessage) {

			super(errorMessage);

		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////

	@ExceptionHandler(WrongPostArgumentsException.class)
	public ResponseEntity<Object> handle(WrongPostArgumentsException exception) {

		ApiError apiError = new ApiError(BAD_REQUEST);    // 400
		apiError.setMessage(exception.getMessage());

		return buildResponseEntity(apiError);

	}

	public static final class WrongPostArgumentsException extends IllegalArgumentException {

		public WrongPostArgumentsException(String errorMessage) {

			super(errorMessage);

		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////

	@ExceptionHandler(WrongPutArgumentsException.class)
	public ResponseEntity<Object> handle(WrongPutArgumentsException exception) {

		ApiError apiError = new ApiError(BAD_REQUEST);    // 400
		apiError.setMessage(exception.getMessage());

		return buildResponseEntity(apiError);

	}

	public static final class WrongPutArgumentsException extends IllegalArgumentException {

		public WrongPutArgumentsException(String errorMessage) {

			super(errorMessage);

		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////

	@ExceptionHandler(WrongDateFormatException.class)
	public ResponseEntity<Object> handle(WrongDateFormatException exception) {

		ApiError apiError = new ApiError(BAD_REQUEST);    // 400
		apiError.setMessage(exception.getMessage());

		return buildResponseEntity(apiError);

	}

	public static final class WrongDateFormatException extends IllegalArgumentException {

		public WrongDateFormatException(String errorMessage) {

			super(errorMessage);

		}

	}
}
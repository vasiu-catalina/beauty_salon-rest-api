package com.vasiu_catalina.beauty_salon;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import java.time.format.DateTimeParseException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.vasiu_catalina.beauty_salon.exception.ErrorResponse;
import com.vasiu_catalina.beauty_salon.exception.appointment.*;
import com.vasiu_catalina.beauty_salon.exception.client.*;
import com.vasiu_catalina.beauty_salon.exception.employee.*;
import com.vasiu_catalina.beauty_salon.exception.product.*;
import com.vasiu_catalina.beauty_salon.exception.service.*;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            // get field name or object name based on error type (field or object specific)
            String fieldName = (error instanceof FieldError) ? ((FieldError) error).getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage(); // default error message
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(new ErrorResponse(errors), status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException ex,
            @NonNull org.springframework.http.HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        String type = "error";
        String message = "Invalid request body provided.";

        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException ife) {
            type = ife.getPath()
                    .stream()
                    .map(ref -> ref.getFieldName())
                    .findFirst()
                    .orElse("unknown field");

            if (ife.getTargetType() == BigDecimal.class) {
                if (ife.getValue() != null && ife.getValue().toString().trim().isEmpty()) {
                    message = "Field '" + type + "' cannot be empty. A valid number is required.";
                } else {
                    message = "Invalid number format.";
                }
            } else if (ife.getCause() instanceof DateTimeParseException) {
                message = "Invalid date provided. Expected format is YYYY-MM-DD.";

            } else if (ife.getCause() instanceof JsonParseException) {
                message = "Invalid format provided. " + ife.getCause().getMessage();

            } else {
                message = ife.getOriginalMessage();
            }
        } else if (cause instanceof JsonMappingException jpe) {
            type = jpe.getPath()
                    .stream()
                    .map(ref -> ref.getFieldName())
                    .findFirst()
                    .orElse("unknown field");

            message = cause.getCause().getMessage();

            if (message.contains("salary")) {
                message = "Salary is required.";

            } else if (message.contains("price")) {
                message = "Price is required.";

            } else if (message.contains("duration")) {
                message = "Duration is required.";
            }

        }
        errors.put(type, message);
        return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            AppointmentNotFoundException.class,
            ClientNotFoundException.class,
            EmployeeNotFoundException.class,
            ProductNotFoundException.class,
            ServiceNotFoundException.class
    })
    public ResponseEntity<Object> handleResourceNotFoundException(RuntimeException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());

        ErrorResponse error = new ErrorResponse(errors);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            AppointmentAlreadyExistsException.class,
            ClientAlreadyExistsException.class,
            EmployeeAlreadyExistsException.class,
            ProductAlreadyExistsException.class,
            ServiceAlreadyExistsException.class
    })
    public ResponseEntity<Object> handleEntityAlreadyExistsExeception(DataIntegrityViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());

        ErrorResponse error = new ErrorResponse(errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}

package com.vasiu_catalina.beauty_salon;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import com.vasiu_catalina.beauty_salon.exception.ErrorResponse;
import com.vasiu_catalina.beauty_salon.exception.appointment.*;
import com.vasiu_catalina.beauty_salon.exception.client.*;
import com.vasiu_catalina.beauty_salon.exception.employee.*;
import com.vasiu_catalina.beauty_salon.exception.product.*;
import com.vasiu_catalina.beauty_salon.exception.review.*;
import com.vasiu_catalina.beauty_salon.exception.service.*;
import com.vasiu_catalina.beauty_salon.security.exception.PasswordsDontMatchException;

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
        String message = "Invalid JSON provided";

        Throwable cause = ex.getCause();

        if (cause instanceof MismatchedInputException mie) {
            errors = this.mismatchedInpuException(mie);
        } else 
        if (cause instanceof JsonMappingException jpe) {
            errors = this.jsonMappingException(jpe);
        } else 
        {
            errors.put(type, message);
        }
        return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            AppointmentNotFoundException.class,
            ClientNotFoundException.class,
            EmployeeNotFoundException.class,
            ProductNotFoundException.class,
            ReviewNotFoundException.class,
            ServiceNotFoundException.class,
            ProductNotFoundForServiceException.class,
            PasswordsDontMatchException.class
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
            ReviewAlreadyExistsException.class,
            ServiceAlreadyExistsException.class,
            ProductAlreadyAddedToServiceException.class
    })
    public ResponseEntity<Object> handleEntityAlreadyExistsExeception(DataIntegrityViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());

        ErrorResponse error = new ErrorResponse(errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private HashMap<String, String> jsonMappingException(JsonMappingException ex) {
        String fieldName = ex.getPath()
                .stream()
                .map(ref -> ref.getFieldName())
                .findFirst()
                .orElse("error");

        String message = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1) + " is required.";

        HashMap<String, String> error = new HashMap<>();
        error.put(fieldName, message);

        return error;
    }

    private HashMap<String, String> mismatchedInpuException(MismatchedInputException ex) {

        String fieldName = ex.getPath()
                .stream()
                .map(ref -> ref.getFieldName())
                .findFirst()
                .orElse("error");

        String message = "Invalid request body provided.";
        HashMap<String, String> error = new HashMap<>();

        if (ex.getTargetType() == BigDecimal.class || ex.getTargetType() == Integer.class) {
            message = "Invalid number provided.";

        } else if (ex.getTargetType() == LocalDate.class) {
            message = "Invalid date provided. Expected format is yyyy-mm-dd.";

        } else if (ex.getTargetType() == LocalDateTime.class) {
            message = "Invalid date provided. Expected format is yyyy-mm-dd'T'hh:mm:ss.";
        }

        error.put(fieldName, message);

        return error;
    }

}

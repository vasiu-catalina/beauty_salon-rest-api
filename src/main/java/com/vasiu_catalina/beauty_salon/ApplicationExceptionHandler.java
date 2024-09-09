package com.vasiu_catalina.beauty_salon;

import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.time.format.DateTimeParseException;

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

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.vasiu_catalina.beauty_salon.exception.ClientNotFoundException;
import com.vasiu_catalina.beauty_salon.exception.ErrorResponse;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

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
            HttpMessageNotReadableException ex, 
            org.springframework.http.HttpHeaders headers, 
            HttpStatusCode status, 
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        String type;
        String message;

        Throwable cause = ex.getCause();
        if ( ex.getCause()instanceof InvalidFormatException) {
            if (ex.getCause().getCause() instanceof DateTimeParseException) {
                type = "date";
                message = "Invalid date provided. Expected format is YYYY-MM-DD.";
            } else {
                type = "error";
                message = "Invalid input: " + (ex.getCause().getCause() != null ? ex.getCause().getCause().getMessage() : "Unknown cause");
            }
        } else {
            type = "general";
            message = Optional.ofNullable(cause)
            .map(Throwable::getMessage)
            .orElse("Invalid input data");
        }
        errors.put(type, message);

        ErrorResponse errorResponse = new ErrorResponse(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(RuntimeException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(errors);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}

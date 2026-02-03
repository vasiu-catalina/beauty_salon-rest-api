package com.vasiu_catalina.beauty_salon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.core.MethodParameter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonParser;
import com.vasiu_catalina.beauty_salon.exception.ErrorResponse;
import com.vasiu_catalina.beauty_salon.exception.client.ClientAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.client.ClientNotFoundException;

class ApplicationExceptionHandlerTest {

    private final ApplicationExceptionHandler handler = new ApplicationExceptionHandler();

    static class DummyRequest {
        String name;
    }

    static class DummyController {
        @SuppressWarnings("unused")
        void create(DummyRequest request) {
        }
    }

    static class NumberPayload {
        public Integer age;
    }

    static class DatePayload {
        public LocalDate date;
    }

    static class DateTimePayload {
        public LocalDateTime dateTime;
    }

    @Test
    void handleMethodArgumentNotValidBuildsErrorResponse() throws Exception {
        DummyRequest target = new DummyRequest();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "dummyRequest");
        bindingResult.addError(new FieldError("dummyRequest", "name", "Name is required"));

        MethodParameter parameter = new MethodParameter(
                DummyController.class.getDeclaredMethod("create", DummyRequest.class), 0);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(
                ex,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                new ServletWebRequest(new MockHttpServletRequest()));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Name is required", error.getErrors().get("name"));
    }

    @Test
    void handleMethodArgumentNotValidWithObjectErrorUsesObjectName() throws Exception {
        DummyRequest target = new DummyRequest();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "dummyRequest");
        bindingResult.addError(new ObjectError("dummyRequest", "Invalid payload"));

        MethodParameter parameter = new MethodParameter(
                DummyController.class.getDeclaredMethod("create", DummyRequest.class), 0);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(
                ex,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                new ServletWebRequest(new MockHttpServletRequest()));

        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Invalid payload", error.getErrors().get("dummyRequest"));
    }

    @Test
    void handleHttpMessageNotReadableWithMismatchedInput() throws IOException {
        MismatchedInputException cause = assertThrows(MismatchedInputException.class, () ->
                new ObjectMapper().readValue("{\"age\":\"bad\"}", NumberPayload.class));

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(
                "Bad input",
                cause,
                new MockHttpInputMessage(new byte[0]));

        ResponseEntity<Object> response = handler.handleHttpMessageNotReadable(
                ex,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                new ServletWebRequest(new MockHttpServletRequest()));

        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Invalid number provided.", error.getErrors().get("age"));
    }

    @Test
    void handleHttpMessageNotReadableWithLocalDate() throws IOException {
        MismatchedInputException cause = MismatchedInputException.from((JsonParser) null, LocalDate.class,
                "Invalid date");
        cause.prependPath(new JsonMappingException.Reference(DatePayload.class, "date"));

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(
                "Bad input",
                cause,
                new MockHttpInputMessage(new byte[0]));

        ResponseEntity<Object> response = handler.handleHttpMessageNotReadable(
                ex,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                new ServletWebRequest(new MockHttpServletRequest()));

        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Invalid date provided. Expected format is yyyy-mm-dd.", error.getErrors().get("date"));
    }

    @Test
    void handleHttpMessageNotReadableWithLocalDateTime() throws IOException {
        MismatchedInputException cause = MismatchedInputException.from((JsonParser) null, LocalDateTime.class,
                "Invalid date time");
        cause.prependPath(new JsonMappingException.Reference(DateTimePayload.class, "dateTime"));

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(
                "Bad input",
                cause,
                new MockHttpInputMessage(new byte[0]));

        ResponseEntity<Object> response = handler.handleHttpMessageNotReadable(
                ex,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                new ServletWebRequest(new MockHttpServletRequest()));

        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Invalid date provided. Expected format is yyyy-mm-dd'T'hh:mm:ss.",
                error.getErrors().get("dateTime"));
    }

    @Test
    void handleHttpMessageNotReadableWithDefaultMismatchedInput() throws IOException {
        MismatchedInputException cause = assertThrows(MismatchedInputException.class, () ->
                new ObjectMapper().readValue("[1,2]", DummyRequest.class));

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(
                "Bad input",
                cause,
                new MockHttpInputMessage(new byte[0]));

        ResponseEntity<Object> response = handler.handleHttpMessageNotReadable(
                ex,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                new ServletWebRequest(new MockHttpServletRequest()));

        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Invalid request body provided.", error.getErrors().get("error"));
    }

    @Test
    void handleHttpMessageNotReadableWithJsonMapping() {
        JsonMappingException cause = JsonMappingException.from((JsonParser) null, "Missing field");
        cause.prependPath(new JsonMappingException.Reference(DummyRequest.class, "name"));

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(
                "Bad input",
                cause,
                new MockHttpInputMessage(new byte[0]));

        ResponseEntity<Object> response = handler.handleHttpMessageNotReadable(
                ex,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                new ServletWebRequest(new MockHttpServletRequest()));

        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Name is required.", error.getErrors().get("name"));
    }

    @Test
    void handleHttpMessageNotReadableWithUnknownCauseUsesDefaultMessage() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(
                "Bad input",
                new RuntimeException("boom"),
                new MockHttpInputMessage(new byte[0]));

        ResponseEntity<Object> response = handler.handleHttpMessageNotReadable(
                ex,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                new ServletWebRequest(new MockHttpServletRequest()));

        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Invalid JSON provided", error.getErrors().get("error"));
    }

    @Test
    void handleResourceNotFoundExceptionReturnsNotFound() {
        ResponseEntity<Object> response = handler.handleResourceNotFoundException(new ClientNotFoundException(1L));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Client with ID 1 was not found.", error.getErrors().get("message"));
    }

    @Test
    void handleEntityAlreadyExistsExceptionReturnsBadRequest() {
        ResponseEntity<Object> response = handler.handleEntityAlreadyExistsExeception(
                new ClientAlreadyExistsException("Email"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Email already taken.", error.getErrors().get("message"));
    }
}

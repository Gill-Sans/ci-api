package com.capit.exceptions.handling;

import com.capit.exceptions.BaseRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private RestExceptionHandler exceptionHandler;

    @BeforeEach
    public void setUp() {
        when(request.getMethod()).thenReturn("GET");
        when(request.getServletPath()).thenReturn("/api/test");
    }

    @Test
    public void testHandleBaseRuntimeException_BadRequest() {
        // Arrange
        String errorMessage = "Bad request error";
        BaseRuntimeException exception = new BaseRuntimeException(errorMessage, HttpStatus.BAD_REQUEST);

        // Act
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleBaseRuntimeException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, Objects.requireNonNull(response.getBody()).message());
        assertEquals(HttpStatus.BAD_REQUEST, response.getBody().status());
    }

    @Test
    public void testHandleBaseRuntimeException_NotFound() {
        // Arrange
        String errorMessage = "Resource not found";
        BaseRuntimeException exception = new BaseRuntimeException(errorMessage, HttpStatus.NOT_FOUND);

        // Act
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleBaseRuntimeException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, Objects.requireNonNull(response.getBody()).message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }

    @Test
    public void testHandleBaseRuntimeException_InternalServerError() {
        // Arrange
        String errorMessage = "Internal server error";
        BaseRuntimeException exception = new BaseRuntimeException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);

        // Act
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleBaseRuntimeException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(errorMessage, Objects.requireNonNull(response.getBody()).message());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getBody().status());
    }

    @Test
    public void testHandleBaseRuntimeException_Unauthorized() {
        // Arrange
        String errorMessage = "Unauthorized access";
        BaseRuntimeException exception = new BaseRuntimeException(errorMessage, HttpStatus.UNAUTHORIZED);

        // Act
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleBaseRuntimeException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(errorMessage, Objects.requireNonNull(response.getBody()).message());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getBody().status());
    }

    @Test
    public void testHandleBaseRuntimeException_EmptyMessage() {
        // Arrange
        String errorMessage = "";
        BaseRuntimeException exception = new BaseRuntimeException(errorMessage, HttpStatus.BAD_REQUEST);

        // Act
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleBaseRuntimeException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, Objects.requireNonNull(response.getBody()).message());
    }

    @Test
    public void testHandleBaseRuntimeException_DifferentRequestMethod() {
        // Arrange
        when(request.getMethod()).thenReturn("POST");
        String errorMessage = "Method not allowed";
        BaseRuntimeException exception = new BaseRuntimeException(errorMessage, HttpStatus.METHOD_NOT_ALLOWED);

        // Act
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleBaseRuntimeException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals(errorMessage, Objects.requireNonNull(response.getBody()).message());
    }

    @Test
    public void testHandleBaseRuntimeException_DifferentServletPath() {
        // Arrange
        when(request.getServletPath()).thenReturn("/api/different");
        String errorMessage = "Different path error";
        BaseRuntimeException exception = new BaseRuntimeException(errorMessage, HttpStatus.BAD_REQUEST);

        // Act
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleBaseRuntimeException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, Objects.requireNonNull(response.getBody()).message());
    }
}

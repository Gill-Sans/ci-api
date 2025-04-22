package com.capit.exceptions.handling;


import com.capit.exceptions.BaseRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleBaseRuntimeException(BaseRuntimeException ex, HttpServletRequest request) {
        return logAndReturnError(ex, ex.getStatus(), request);
    }


    private ResponseEntity<ExceptionResponse> logAndReturnError(Throwable throwable, HttpStatus status, HttpServletRequest request) {
        log.error("[Error] {} {} - with message: {}", request.getMethod(), request.getServletPath(), throwable.getMessage());
        log.debug("Stack trace: ", throwable);
        return new ResponseEntity<>(new ExceptionResponse(throwable.getMessage(), status), status);
    }
}

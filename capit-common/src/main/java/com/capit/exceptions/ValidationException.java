package com.capit.exceptions;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public class ValidationException extends BaseRuntimeException {
    private static final String BASE_MESSAGE = "Failed to validate";
    private static final HttpStatus BASE_HTTP_STATUS = HttpStatus.BAD_REQUEST;

    public ValidationException(@NotNull String message) {
        super(message, BASE_HTTP_STATUS);
    }

    public ValidationException(Class<?> clazz, String fieldName, String constraint) {
        this("%s %s: %s %s".formatted(BASE_MESSAGE, clazz.getSimpleName(), fieldName, constraint));
    }
}

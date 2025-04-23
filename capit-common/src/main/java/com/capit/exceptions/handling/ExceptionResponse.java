package com.capit.exceptions.handling;

import org.springframework.http.HttpStatus;

public record ExceptionResponse(String message, HttpStatus status) {

}

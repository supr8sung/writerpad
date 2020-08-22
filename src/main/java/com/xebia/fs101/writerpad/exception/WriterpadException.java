package com.xebia.fs101.writerpad.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@Slf4j
public class WriterpadException extends RuntimeException {

    private String message;

    private HttpStatus httpStatus;

    public WriterpadException(String message) {
        this.message = message;
        this.httpStatus = INTERNAL_SERVER_ERROR;
    }

    public WriterpadException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

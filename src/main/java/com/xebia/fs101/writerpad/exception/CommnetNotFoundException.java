package com.xebia.fs101.writerpad.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class CommnetNotFoundException extends RuntimeException {
    public CommnetNotFoundException() {

    }
}

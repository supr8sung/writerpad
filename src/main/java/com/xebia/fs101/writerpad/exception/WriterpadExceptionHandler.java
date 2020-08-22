package com.xebia.fs101.writerpad.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WriterpadExceptionHandler {

    @ExceptionHandler(WriterpadException.class)
    public ResponseEntity<String> genericHandler(WriterpadException writerpadException) {
        return ResponseEntity.status(writerpadException.getHttpStatus())
                .body(writerpadException.getMessage());
    }
}

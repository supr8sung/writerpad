package com.xebia.fs101.writerpad.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ArticleAlreadyCreatedException extends RuntimeException {
    public ArticleAlreadyCreatedException(String exception) {

        super(exception);
    }
}

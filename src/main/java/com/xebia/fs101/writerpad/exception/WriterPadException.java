package com.xebia.fs101.writerpad.exception;

public class WriterPadException extends RuntimeException {

    private Exception ex;
    private Object context;

    public WriterPadException(Exception ex) {

    }

    public Exception getEx() {

        return ex;
    }

    public Object getContext() {

        return context;
    }

}

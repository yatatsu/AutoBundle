package com.yatatsu.autobundle.processor.exceptions;

public class ProcessingException extends RuntimeException {

    public ProcessingException(String message, Object... args) {
        super(String.format(message, args));
    }

    public ProcessingException(String message, Throwable throwable, Object... args) {
        super(String.format(message, args), throwable);
    }
}


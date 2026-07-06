package com.roshan.minispring.exception;

public class MiniSpringException extends RuntimeException {

    public MiniSpringException(String message){
        super(message);
    }

    public MiniSpringException(String message, Throwable cause){
        super(message, cause);
    }
}

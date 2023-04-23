package com.medical.center.base.exception;

public abstract class BaseException extends RuntimeException {

    BaseException(String message){
        super(message);
    }
}

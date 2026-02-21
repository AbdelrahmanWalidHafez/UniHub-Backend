package com.unihub.auth.common.exception.model;

public class MaxUserAmountExceededException extends RuntimeException {
    public MaxUserAmountExceededException(String message) {
        super(message);
    }
}

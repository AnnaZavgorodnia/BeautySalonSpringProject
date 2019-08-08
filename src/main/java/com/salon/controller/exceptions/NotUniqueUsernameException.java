package com.salon.controller.exceptions;

public class NotUniqueUsernameException extends RuntimeException {
    public NotUniqueUsernameException() {
    }

    public NotUniqueUsernameException(String s) {
        super(s);
    }

    public NotUniqueUsernameException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NotUniqueUsernameException(Throwable throwable) {
        super(throwable);
    }
}

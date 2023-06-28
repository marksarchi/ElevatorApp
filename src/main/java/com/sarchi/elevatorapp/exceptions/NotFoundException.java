package com.sarchi.elevatorapp.exceptions;
import org.slf4j.helpers.MessageFormatter;

public class NotFoundException extends Exception{
    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message, Object... args) {
        super(MessageFormatter.arrayFormat(message, args).getMessage());
    }

    public NotFoundException(Throwable cause, String message, Object... args) {
        super(MessageFormatter.arrayFormat(message, args).getMessage(), cause);
    }
}

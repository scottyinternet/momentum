package com.nashss.se.momentum.exceptions;

public class EventNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 210391313713849061L;

    public EventNotFoundException(){
        super();
    }

    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException(Throwable cause) {
        super(cause);
    }

    public EventNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}

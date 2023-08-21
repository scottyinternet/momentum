package com.nashss.se.musicplaylistservice.exceptions;

/**
 * Exception to throw when a provided value has invalid attribute values.
 */
public class InvalidAttributeValueException extends InvalidAttributeException {

    private static final long serialVersionUID = 8007453316698012851L;

    /**
     * Exception with no message or cause.
     */
    public InvalidAttributeValueException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public InvalidAttributeValueException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public InvalidAttributeValueException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public InvalidAttributeValueException(String message, Throwable cause) {
        super(message, cause);
    }
}

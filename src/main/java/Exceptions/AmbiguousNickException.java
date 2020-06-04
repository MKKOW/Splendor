package Exceptions;

/**
 * Exception thrown when instantiating ServerBoard with not unique nicks
 */
public class AmbiguousNickException extends Exception {

    /**
     * Basic exception
     */
    public AmbiguousNickException() {
    }

    /**
     * Exception with message
     *
     * @param message - message explaining exception
     */
    public AmbiguousNickException(String message) {
        super(message);
    }

    /**
     * Exception with message and cause
     *
     * @param message - message explaining exception
     * @param cause   - Exception that caused that exception
     */
    public AmbiguousNickException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Exception with cause
     *
     * @param cause - Exception that caused that exception
     */
    public AmbiguousNickException(Throwable cause) {
        super(cause);
    }

    /**
     * Full information exception
     *
     * @param message            - message explaining exception
     * @param cause              - Exception that caused that exception
     * @param enableSuppression  -
     * @param writableStackTrace -
     */
    public AmbiguousNickException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

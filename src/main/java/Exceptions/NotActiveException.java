package Exceptions;

/**
 * Exception thrown when some action is performed on
 * player that is not active.
 */
public class NotActiveException extends Exception {

    /**
     * Basic exception
     */
    public NotActiveException() {
    }

    /**
     * Exception with message
     *
     * @param message - message explaining exception
     */
    public NotActiveException(String message) {
        super(message);
    }

    /**
     * Exception with message and cause
     *
     * @param message - message explaining exception
     * @param cause   - Exception that caused that exception
     */
    public NotActiveException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Exception with cause
     *
     * @param cause - Exception that caused that exception
     */
    public NotActiveException(Throwable cause) {
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
    public NotActiveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

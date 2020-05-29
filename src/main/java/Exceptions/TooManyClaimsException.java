package Exceptions;

/**
 * Exception thrown when player is ordered to hold
 * some development card, but he already has one on
 * his hand.
 */
public class TooManyClaimsException extends Exception {

    /**
     * Basic exception
     */
    public TooManyClaimsException() {
    }

    /**
     * Exception with message
     *
     * @param message - message explaining exception
     */
    public TooManyClaimsException(String message) {
        super(message);
    }

    /**
     * Exception with message and cause
     *
     * @param message - message explaining exception
     * @param cause   - Exception that caused that exception
     */
    public TooManyClaimsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Exception with cause
     *
     * @param cause - Exception that caused that exception
     */
    public TooManyClaimsException(Throwable cause) {
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
    public TooManyClaimsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

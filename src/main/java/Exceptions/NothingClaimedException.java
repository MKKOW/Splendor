package Exceptions;

/**
 * Exception thrown when player is ordered to remove
 * card from his hand, but do not have any card on hand
 */
public class NothingClaimedException extends Exception {

    /**
     * Basic exception
     */
    public NothingClaimedException() {
    }

    /**
     * Exception with message
     *
     * @param message - message explaining exception
     */
    public NothingClaimedException(String message) {
        super(message);
    }

    /**
     * Exception with message and cause
     *
     * @param message - message explaining exception
     * @param cause   - Exception that caused that exception
     */
    public NothingClaimedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Exception with cause
     *
     * @param cause - Exception that caused that exception
     */
    public NothingClaimedException(Throwable cause) {
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
    public NothingClaimedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

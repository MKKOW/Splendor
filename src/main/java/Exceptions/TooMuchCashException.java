package Exceptions;

/**
 * Exception thrown when player is given cash, but it overflows
 * max allowed sum of cash to have.
 */
public class TooMuchCashException extends Exception {

    /**
     * Basic exception
     */
    public TooMuchCashException() {
    }

    /**
     * Exception with message
     *
     * @param message - message explaining exception
     */
    public TooMuchCashException(String message) {
        super(message);
    }

    /**
     * Exception with message and cause
     *
     * @param message - message explaining exception
     * @param cause   - Exception that caused that exception
     */
    public TooMuchCashException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Exception with cause
     *
     * @param cause - Exception that caused that exception
     */
    public TooMuchCashException(Throwable cause) {
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
    public TooMuchCashException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

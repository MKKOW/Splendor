package Exceptions;

/**
 * Exception thrown when there is no enough cash to subtract from
 * i.e when subtracting from cash becomes negative.
 */
public class NotEnoughCashException extends Exception {

    /**
     * Basic exception
     */
    public NotEnoughCashException() {
    }

    /**
     * Exception with message
     *
     * @param message - message explaining exception
     */
    public NotEnoughCashException(String message) {
        super(message);
    }

    /**
     * Exception with message and cause
     *
     * @param message - message explaining exception
     * @param cause   - Exception that caused that exception
     */
    public NotEnoughCashException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Exception with cause
     *
     * @param cause - Exception that caused that exception
     */
    public NotEnoughCashException(Throwable cause) {
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
    public NotEnoughCashException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

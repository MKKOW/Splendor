package Exceptions;

/**
 * Exception thrown when noble is added to players hand,
 * but player do not have enough discount to get that noble
 */
public class NotEnoughDiscountException extends Exception {

    /**
     * Basic exception
     */
    public NotEnoughDiscountException() {
    }

    /**
     * Exception with message
     *
     * @param message - message explaining exception
     */
    public NotEnoughDiscountException(String message) {
        super(message);
    }

    /**
     * Exception with message and cause
     *
     * @param message - message explaining exception
     * @param cause   - Exception that caused that exception
     */
    public NotEnoughDiscountException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Exception with cause
     *
     * @param cause - Exception that caused that exception
     */
    public NotEnoughDiscountException(Throwable cause) {
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
    public NotEnoughDiscountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

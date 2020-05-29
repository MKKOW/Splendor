package Exceptions;

/**
 * Exception thrown when
 */
public class IllegalCashAmountException extends Exception {

    public IllegalCashAmountException() {
    }

    public IllegalCashAmountException(String message) {
        super(message);
    }

    public IllegalCashAmountException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalCashAmountException(Throwable cause) {
        super(cause);
    }

    public IllegalCashAmountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

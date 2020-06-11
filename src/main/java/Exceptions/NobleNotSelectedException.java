package Exceptions;

/**
 * Exception thrown when active player is changed
 * but can be visited by noble on the board
 */
public class NobleNotSelectedException extends Exception{
    public NobleNotSelectedException() {
    }

    public NobleNotSelectedException(String message) {
        super(message);
    }

    public NobleNotSelectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NobleNotSelectedException(Throwable cause) {
        super(cause);
    }

    public NobleNotSelectedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

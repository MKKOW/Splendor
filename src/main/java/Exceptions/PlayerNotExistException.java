package Exceptions;

/**
 * Exception thrown when player of given nick do not exists
 */
public class PlayerNotExistException extends RuntimeException {

    public PlayerNotExistException() {
    }

    public PlayerNotExistException(String message) {
        super(message);
    }

    public PlayerNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerNotExistException(Throwable cause) {
        super(cause);
    }

    public PlayerNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

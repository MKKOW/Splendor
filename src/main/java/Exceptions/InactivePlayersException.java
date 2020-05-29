package Exceptions;

public class InactivePlayersException extends Exception {

    public InactivePlayersException() {
    }

    public InactivePlayersException(String message) {
        super(message);
    }

    public InactivePlayersException(String message, Throwable cause) {
        super(message, cause);
    }

    public InactivePlayersException(Throwable cause) {
        super(cause);
    }

    public InactivePlayersException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

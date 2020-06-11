package Exceptions;


public class TooManyPlayersException extends Exception {

    public TooManyPlayersException() {
    }

    public TooManyPlayersException(String message) {
        super(message);
    }

    public TooManyPlayersException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyPlayersException(Throwable cause) {
        super(cause);
    }

    public TooManyPlayersException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

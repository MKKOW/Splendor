package Exceptions;

/**
 * Exception thrown when card is not found on board
 */
public class CardNotOnBoardException extends Exception {

    public CardNotOnBoardException() {
    }

    public CardNotOnBoardException(String message) {
        super(message);
    }

    public CardNotOnBoardException(String message, Throwable cause) {
        super(message, cause);
    }

    public CardNotOnBoardException(Throwable cause) {
        super(cause);
    }

    public CardNotOnBoardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

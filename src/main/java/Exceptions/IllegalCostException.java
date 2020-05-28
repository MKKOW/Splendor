package Exceptions;

public class IllegalCostException extends IllegalArgumentException {

    public IllegalCostException() {
    }

    public IllegalCostException(String s) {
        super(s);
    }

    public IllegalCostException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalCostException(Throwable cause) {
        super(cause);
    }
}

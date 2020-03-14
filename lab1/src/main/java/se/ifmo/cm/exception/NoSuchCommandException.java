package se.ifmo.cm.exception;

public class NoSuchCommandException extends Exception {

    public NoSuchCommandException() {
        super();
    }

    public NoSuchCommandException(String message) {
        super(message);
    }
}

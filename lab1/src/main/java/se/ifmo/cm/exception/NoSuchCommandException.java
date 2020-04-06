package se.ifmo.cm.exception;

public class NoSuchCommandException extends RuntimeException {

    public NoSuchCommandException(String message) {
        super(message);
    }
}

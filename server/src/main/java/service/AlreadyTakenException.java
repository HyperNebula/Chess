package service;

public class AlreadyTakenException extends RuntimeException {
    public AlreadyTakenException(int code, String message) {
        super(message);
    }
}

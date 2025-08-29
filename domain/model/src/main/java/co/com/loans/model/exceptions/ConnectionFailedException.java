package co.com.loans.model.exceptions;

import lombok.Getter;

@Getter
public class ConnectionFailedException extends RuntimeException {
    private final int status;
    public ConnectionFailedException(int status, String message) {
        super(message);
        this.status = status;
    }
}

package co.com.loans.model.exceptions;

import lombok.Getter;

@Getter
public class UserDoesNotExistException extends RuntimeException {
    private final int status;
    public UserDoesNotExistException(int status, String message) {
        super(message);
        this.status = status;
    }
}

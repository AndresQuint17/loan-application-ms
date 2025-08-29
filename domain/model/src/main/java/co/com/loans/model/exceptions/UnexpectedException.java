package co.com.loans.model.exceptions;

import lombok.Getter;

@Getter
public class UnexpectedException extends RuntimeException {
    private final int status;
    public UnexpectedException(int status, String message) {
        super(message);
        this.status = status;
    }
}

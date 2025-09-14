package co.com.loans.model.exceptions;

import lombok.Getter;

@Getter
public class LoanTypeDoesNotExistException extends RuntimeException {
    private final int status;
    public LoanTypeDoesNotExistException(int status, String message) {
        super(message);
        this.status = status;
    }
}

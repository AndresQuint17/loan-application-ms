package co.com.loans.model.exceptions;

import lombok.Getter;

@Getter
public class SavingInDatabaseException extends RuntimeException {
    private final int status;
    public SavingInDatabaseException(int status, String message) {
        super(message);
        this.status = status;
    }
}

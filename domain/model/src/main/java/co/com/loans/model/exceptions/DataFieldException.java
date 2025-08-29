package co.com.loans.model.exceptions;

import lombok.Getter;

@Getter
public class DataFieldException extends RuntimeException {
    private final int status;
    public DataFieldException(int status, String message) {
        super(message);
        this.status = status;
    }
}

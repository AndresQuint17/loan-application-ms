package co.com.loans.model.exceptions;

import lombok.Getter;

@Getter
public class DataFieldException extends RuntimeException {
    public DataFieldException(String message) {
        super(message);
    }
}

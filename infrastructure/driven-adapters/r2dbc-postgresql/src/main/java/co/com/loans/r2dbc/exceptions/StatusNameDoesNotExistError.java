package co.com.loans.r2dbc.exceptions;

import lombok.Getter;

@Getter
public class StatusNameDoesNotExistError extends RuntimeException{
    public StatusNameDoesNotExistError(String message) {
        super(message);
    }
}

package co.com.loans.api.exception;

import lombok.Getter;

@Getter
public class BadTokenError extends RuntimeException{
    public BadTokenError(String message) {
        super(message);
    }
}

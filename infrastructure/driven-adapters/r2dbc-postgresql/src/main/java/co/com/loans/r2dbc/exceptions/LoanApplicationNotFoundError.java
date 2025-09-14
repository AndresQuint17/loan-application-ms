package co.com.loans.r2dbc.exceptions;

import lombok.Getter;

@Getter
public class LoanApplicationNotFoundError extends RuntimeException{
    public LoanApplicationNotFoundError(String message) {
        super(message);
    }
}

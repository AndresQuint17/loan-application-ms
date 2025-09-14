package co.com.loans.usecase.registerLoanApplication.exceptions;

import lombok.Getter;

@Getter
public class UserDoesNotMatchError extends RuntimeException{
    public UserDoesNotMatchError(String message) {
        super(message);
    }
}

package co.com.loans.usecase.changeLoanApplicationStatus.exceptions;

import lombok.Getter;

@Getter
public class LoanApplicationIdError extends RuntimeException{
    public LoanApplicationIdError(String message) {
        super(message);
    }
}

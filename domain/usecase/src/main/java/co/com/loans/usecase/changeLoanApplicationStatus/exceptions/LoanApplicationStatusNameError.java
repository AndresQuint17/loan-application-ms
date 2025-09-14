package co.com.loans.usecase.changeLoanApplicationStatus.exceptions;

import lombok.Getter;

@Getter
public class LoanApplicationStatusNameError extends RuntimeException{
    public LoanApplicationStatusNameError(String message) {
        super(message);
    }
}

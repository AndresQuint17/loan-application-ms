package co.com.loans.usecase.listLoanApplications.exceptions;

import lombok.Getter;

@Getter
public class LoanApplicationsNotFoundException extends RuntimeException{
    public LoanApplicationsNotFoundException(String message) {
        super(message);
    }
}

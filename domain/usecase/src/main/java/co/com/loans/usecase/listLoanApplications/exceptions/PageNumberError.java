package co.com.loans.usecase.listLoanApplications.exceptions;

import lombok.Getter;

@Getter
public class PageNumberError extends RuntimeException{
    public PageNumberError(String message) {
        super(message);
    }
}

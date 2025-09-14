package co.com.loans.usecase.listLoanApplications.exceptions;

import lombok.Getter;

@Getter
public class PageSizeError extends RuntimeException{
    public PageSizeError(String message) {
        super(message);
    }
}

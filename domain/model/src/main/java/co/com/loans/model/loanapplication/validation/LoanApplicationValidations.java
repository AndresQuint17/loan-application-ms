package co.com.loans.model.loanapplication.validation;

import co.com.loans.model.exceptions.DataFieldException;
import co.com.loans.model.loanapplication.LoanApplication;

public class LoanApplicationValidations {
    public static void validate(LoanApplication loanApplication) {
        if (loanApplication.getAmount() == null || loanApplication.getAmount() <= 0 || loanApplication.getAmount() > 15000000) {
            throw new DataFieldException(400, "Amount must be a positive number and less than 15000000.");
        }

        if (loanApplication.getTerm() == null || loanApplication.getTerm() <= 0 || loanApplication.getTerm() > 240) {
            throw new DataFieldException(400, "Term must be a positive integer and less than 240.");
        }
    }

    private static boolean isValidEmail(String email) {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}

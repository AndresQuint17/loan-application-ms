package co.com.loans.model.loantype.validation;

import co.com.loans.model.exceptions.DataFieldException;
import co.com.loans.model.loantype.LoanType;

public class LoanTypeValidations {
    public static void validate(LoanType loanType) {
        if (loanType.getName() == null || loanType.getName().isBlank()) {
            throw new DataFieldException("Loan type name is required.");
        }
    }
}

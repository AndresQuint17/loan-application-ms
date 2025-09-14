package co.com.loans.model.loanapplication.dto;

import java.math.BigDecimal;

public record LoanApplicationDto(
        Long applicationId,
        Double amount,
        Integer term,
        String email,
        String fullName,
        String loanTypeName,
        Double interestRate,
        String statusName,
        BigDecimal baseSalary
) {
}

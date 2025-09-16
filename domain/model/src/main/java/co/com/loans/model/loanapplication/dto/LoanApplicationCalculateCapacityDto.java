package co.com.loans.model.loanapplication.dto;

public record LoanApplicationCalculateCapacityDto(
        Long applicationId,
        Double amount,
        Integer term,
        Double interestRate
) {
}

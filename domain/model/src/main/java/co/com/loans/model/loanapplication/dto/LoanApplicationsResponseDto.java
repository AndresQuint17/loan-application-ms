package co.com.loans.model.loanapplication.dto;

import java.math.BigDecimal;
import java.util.List;

public record LoanApplicationsResponseDto(
        List<LoanApplicationDto> applications,
        BigDecimal totalMonthlyApprovedDebt
) {
}

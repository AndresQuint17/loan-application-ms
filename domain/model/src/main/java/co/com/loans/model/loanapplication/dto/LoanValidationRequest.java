package co.com.loans.model.loanapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanValidationRequest {
    private String idCard;
    private String email;
    private String fullName;
    private BigDecimal baseSalary;
    private List<LoanApplicationCalculateCapacityDto> applicationsApproved;
    private LoanApplicationCalculateCapacityDto newLoan;
}

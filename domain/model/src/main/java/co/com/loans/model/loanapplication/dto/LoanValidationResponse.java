package co.com.loans.model.loanapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanValidationResponse {
    private Long applicationId;
    private String decision;
    private List<PaymentPlanDto> planPago;
}

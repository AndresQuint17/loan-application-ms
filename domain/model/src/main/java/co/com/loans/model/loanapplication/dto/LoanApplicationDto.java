package co.com.loans.model.loanapplication.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationDto {
    private Long applicationId;
    private Double amount;
    private Integer term;
    private String email;
    private String statusId;
    private BigDecimal baseSalary;
}

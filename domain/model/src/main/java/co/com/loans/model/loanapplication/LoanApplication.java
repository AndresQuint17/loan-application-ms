package co.com.loans.model.loanapplication;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanApplication {
    private Double amount;
    private Integer term;
    private String email;
    private Long statusId;
    private Long loanTypeId;
    private BigDecimal baseSalary;
    private String fullName;
}

package co.com.loans.model.loanapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentPlanDto {
    private Integer mes;
    private Double cuota;
    private Double abonoCapital;
    private Double interes;
    private Double saldoRestante;
}

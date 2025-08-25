package co.com.loans.api.model;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* LoanApplicationRequest
*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for a loan application")
public class LoanApplicationRequest {
    @Schema(description = "Customer identification document number", example = "1234567890")
    private String customerId = null;
    @Schema(description = "Requested loan amount", example = "5000.00")
    private Double amount = null;
    @Schema(description = "Loan term in months", example = "24")
    private Integer term = null;
    @Schema(description = "Type of loan requested", example = "personal")
    private String loanType = null;
}
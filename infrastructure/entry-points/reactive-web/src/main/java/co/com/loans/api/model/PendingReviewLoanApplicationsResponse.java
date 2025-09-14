package co.com.loans.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
* LoanApplicationResponse
*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for a loan application submission")
public class PendingReviewLoanApplicationsResponse {
    @Schema(description = "List of paginated loan applications")
    List<LoanApplication> applications;

    @Schema(description = "Total monthly approved debt across all applications")
    BigDecimal totalMonthlyApprovedDebt;
}


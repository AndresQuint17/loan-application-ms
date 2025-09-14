package co.com.loans.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * LoanApplicationRequest
 */

@Schema(description = "Request object for a loan application")
public record ChangeLoanApplicationStatusRequest(
        @Schema(description = "Loan Application ID number", example = "1234567")
        Long loanApplicationId,
        @Schema(description = "New status name to the loan application", example = "APPROVED")
        String newStatus
) {
}
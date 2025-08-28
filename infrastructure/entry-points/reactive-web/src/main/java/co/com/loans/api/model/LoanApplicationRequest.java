package co.com.loans.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * LoanApplicationRequest
 */

@Schema(description = "Request object for a loan application")
public record LoanApplicationRequest(
        @Schema(description = "Customer identification document number", example = "1234567890")
        String idCard,
        @Schema(description = "Requested loan amount", example = "5000.00")
        Double amount,
        @Schema(description = "Loan term in months", example = "24")
        Integer term,
        @Schema(description = "Type of loan requested", example = "personal")
        Long loanTypeId
) {
}
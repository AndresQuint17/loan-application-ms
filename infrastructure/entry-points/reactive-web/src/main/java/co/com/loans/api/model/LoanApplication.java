package co.com.loans.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class LoanApplication {
    @Schema(description = "Unique ID of the application")
    Long applicationId;
    @Schema(description = "Requested loan amount")
    Double amount;
    @Schema(description = "Requested loan term in months")
    Integer term;
    @Schema(description = "Applicant's email address")
    String email;
    @Schema(description = "Applicant's full name")
    String fullName;
    @Schema(description = "Type of loan requested")
    String loanTypeName;
    @Schema(description = "Interest rate for the loan type")
    Double interestRate;
    @Schema(description = "Current status of the application")
    String statusName;
    @Schema(description = "Applicant's base salary")
    BigDecimal baseSalary;
}

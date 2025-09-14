package co.com.loans.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* LoanApplicationResponse
*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for a loan application submission")
public class LoanApplicationResponse {
    @Schema(description = "Status of the loan application", example = "1")
    private String status;
    @Schema(description = "Description of the status of the loan application", example = "Pending approval")
    private String message;
}
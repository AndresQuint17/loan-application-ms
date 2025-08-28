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
    /*@Schema(description = "Unique identifier of the loan application", example = "a12b-34c5")
    private String applicationId = null;*/
    @Schema(description = "Initial status of the application", example = "Pending Review")
    private String status = null;
}
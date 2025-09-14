package co.com.loans.model.loanapplication.gateways;

import co.com.loans.model.loanapplication.LoanApplication;
import co.com.loans.model.loanapplication.dto.LoanApplicationsResponseDto;
import reactor.core.publisher.Mono;

public interface LoanApplicationRepository {
    Mono<LoanApplication> registerLoanApplication(LoanApplication loanApplication);
    Mono<LoanApplicationsResponseDto> findPaginated(int page, int size);
    Mono<String> updateLoanApplicationStatus(Long loanApplicationId, String newStatusId);
}

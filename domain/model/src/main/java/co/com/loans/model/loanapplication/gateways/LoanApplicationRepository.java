package co.com.loans.model.loanapplication.gateways;

import co.com.loans.model.loanapplication.LoanApplication;
import co.com.loans.model.loanapplication.dto.LoanApplicationCalculateCapacityDto;
import co.com.loans.model.loanapplication.dto.LoanApplicationDto;
import co.com.loans.model.loanapplication.dto.LoanApplicationsResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanApplicationRepository {
    Mono<LoanApplicationDto> registerLoanApplication(LoanApplication loanApplication);
    Mono<LoanApplicationsResponseDto> findPaginated(int page, int size);
    Mono<String> updateLoanApplicationStatus(Long loanApplicationId, String newStatusId);
    Flux<LoanApplicationCalculateCapacityDto> listAllApprovedApplicationsOfUser(String idCard);
}

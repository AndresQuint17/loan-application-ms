package co.com.loans.usecase.listLoanApplications;

import co.com.loans.model.loanapplication.dto.LoanApplicationsResponseDto;
import co.com.loans.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.loans.usecase.listLoanApplications.exceptions.LoanApplicationsNotFoundException;
import co.com.loans.usecase.listLoanApplications.exceptions.PageNumberError;
import co.com.loans.usecase.listLoanApplications.exceptions.PageSizeError;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ListLoanApplicationsUseCase {
    private final LoanApplicationRepository loanApplicationRepository;

    public Mono<LoanApplicationsResponseDto> listPendingReviewLoanApplications(int page, int size) {

        if (page < 0) {
            return Mono.error(new PageNumberError("Page number cannot be negative."));
        }
        if (size <= 0) {
            return Mono.error(new PageSizeError("Page size must be a positive value."));
        }

        return loanApplicationRepository.findPaginated(page, size)
                .switchIfEmpty(Mono.error(new LoanApplicationsNotFoundException("No loan applications found.")));
    }
}

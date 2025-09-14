package co.com.loans.usecase.changeLoanApplicationStatus;

import co.com.loans.model.loanapplication.LoanApplication;
import co.com.loans.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.loans.model.loanapplication.gateways.MessageGateway;
import co.com.loans.usecase.changeLoanApplicationStatus.exceptions.LoanApplicationIdError;
import co.com.loans.usecase.changeLoanApplicationStatus.exceptions.LoanApplicationStatusNameError;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ChangeLoanApplicationStatusUseCase {

    private final MessageGateway messageGateway;
    private final LoanApplicationRepository loanApplicationRepository;

    @SneakyThrows
    public Mono<Void> updateStatus(Long loanApplicationId, String newStatus){
        if (loanApplicationId == null || loanApplicationId < 1){
            return Mono.error(new LoanApplicationIdError("loanApplicationId must be a valid number"));
        }
        if (newStatus == null || newStatus.isBlank()){
            return Mono.error(new LoanApplicationStatusNameError("newStatus cannot be null or empty"));
        }
        return loanApplicationRepository.updateLoanApplicationStatus(loanApplicationId, newStatus)
                .flatMap(messageGateway::send)
                .then();
    }
}

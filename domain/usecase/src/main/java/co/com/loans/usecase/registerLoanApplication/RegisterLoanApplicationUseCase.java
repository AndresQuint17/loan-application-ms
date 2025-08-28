package co.com.loans.usecase.registerLoanApplication;

import co.com.loans.model.loanapplication.LoanApplication;
import co.com.loans.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.loans.model.loanapplication.gateways.UserRestConsumerGateway;
import co.com.loans.usecase.exceptions.UserDoesNotExistException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterLoanApplicationUseCase {
    private final LoanApplicationRepository loanApplicationRepository;
    private final UserRestConsumerGateway restConsumer;

    public Mono<LoanApplication> registerLoanApplication(String idCard, LoanApplication loanApplication){
        return restConsumer.getUserByIdCard(idCard)
                .switchIfEmpty(Mono.defer(() -> {
                    // Lanzar la excepción cuando el Mono está vacío
                    return Mono.error(new UserDoesNotExistException(404, "User with idCard " + idCard + " does not exist"));
                }))
                .flatMap(userResponse -> {
                    loanApplication.setEmail(userResponse.getEmail());
                    return loanApplicationRepository.registerLoanApplication(loanApplication);
                })
                .switchIfEmpty(Mono.error(new Exception("Error registrando la solicitud")));
    }
}

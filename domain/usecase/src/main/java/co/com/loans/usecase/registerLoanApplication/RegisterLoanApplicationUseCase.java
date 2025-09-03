package co.com.loans.usecase.registerLoanApplication;

import co.com.loans.model.exceptions.LoanTypeDoesNotExistException;
import co.com.loans.model.exceptions.SavingInDatabaseException;
import co.com.loans.model.loanapplication.LoanApplication;
import co.com.loans.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.loans.model.loanapplication.gateways.UserRestConsumerGateway;
import co.com.loans.model.loantype.gateways.LoanTypeRepository;
import co.com.loans.usecase.registerLoanApplication.exceptions.UserDoesNotMatchError;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterLoanApplicationUseCase {
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final UserRestConsumerGateway restConsumer;

    public Mono<LoanApplication> registerLoanApplication(
            String idCard,
            String loanTypeName,
            LoanApplication loanApplication,
            String bearerToken,
            String subject) {

        if (!idCard.equals(subject)) {
            return Mono.error(new UserDoesNotMatchError("You can only register loan applications for yourself."));
        }

        return restConsumer.getUserByIdCard(idCard, bearerToken)
                .flatMap(userResponse ->
                        loanTypeRepository.getLoanTypeByName(loanTypeName)
                                .switchIfEmpty(Mono.error(new LoanTypeDoesNotExistException(
                                        404,
                                        "Loan type not found: " + loanTypeName)))
                                .flatMap(loanType -> {
                                    // Seteamos loanTypeId con el ID encontrado
                                    loanApplication.setLoanTypeId(loanType.getLoanTypeId());
                                    // Continuamos pasando el userResponse para el siguiente paso
                                    return Mono.just(userResponse);
                                })
                )
                .flatMap(userResponse -> {
                    loanApplication.setEmail(userResponse.getEmail());
                    loanApplication.setStatusId(1L);
                    return loanApplicationRepository.registerLoanApplication(loanApplication);
                })
                .switchIfEmpty(Mono.error(new SavingInDatabaseException(
                        500, "Error registering the request in the database")));
    }
}

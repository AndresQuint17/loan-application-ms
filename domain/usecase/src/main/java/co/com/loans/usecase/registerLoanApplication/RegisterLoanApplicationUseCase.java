package co.com.loans.usecase.registerLoanApplication;

import co.com.loans.model.exceptions.LoanTypeDoesNotExistException;
import co.com.loans.model.exceptions.SavingInDatabaseException;
import co.com.loans.model.loanapplication.LoanApplication;
import co.com.loans.model.loanapplication.dto.LoanApplicationCalculateCapacityDto;
import co.com.loans.model.loanapplication.dto.LoanApplicationDto;
import co.com.loans.model.loanapplication.dto.LoanValidationRequest;
import co.com.loans.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.loans.model.loanapplication.gateways.MessageGateway;
import co.com.loans.model.loanapplication.gateways.UserRestConsumerGateway;
import co.com.loans.model.loanapplication.validation.LoanApplicationValidations;
import co.com.loans.model.loantype.LoanType;
import co.com.loans.model.loantype.gateways.LoanTypeRepository;
import co.com.loans.model.loantype.validation.LoanTypeValidations;
import co.com.loans.model.user.User;
import co.com.loans.model.user.validation.UserValidations;
import co.com.loans.model.loanapplication.mapper.LoanValidationMapper;
import co.com.loans.usecase.registerLoanApplication.exceptions.UserDoesNotMatchError;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.Locale;

@RequiredArgsConstructor
public class RegisterLoanApplicationUseCase {
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final UserRestConsumerGateway restConsumer;
    private final MessageGateway messageGateway;

    public Mono<LoanApplicationDto> registerLoanApplication(
            String idCard,
            String loanTypeName,
            LoanApplication loanApplication,
            String bearerToken,
            String subject) {

        if (!idCard.equals(subject)) {
            return Mono.error(new UserDoesNotMatchError("You can only register loan applications for yourself."));
        }

        LoanApplicationValidations.validate(loanApplication);

        User user = new User();
        user.setIdCard(idCard);
        UserValidations.validate(user);

        LoanType loanType = new LoanType();
        loanType.setName(loanTypeName.trim().toUpperCase(Locale.ROOT));
        LoanTypeValidations.validate(loanType);

        return restConsumer.getUserByIdCard(idCard, bearerToken)
                .flatMap(userResponse ->
                        loanTypeRepository.getLoanTypeByName(loanType.getName())
                                .switchIfEmpty(Mono.error(new LoanTypeDoesNotExistException(
                                        404,
                                        "Loan type not found: " + loanTypeName)))
                                .flatMap(loanTypeRegistered -> {
                                    loanApplication.setLoanTypeId(loanTypeRegistered.getLoanTypeId());
                                    return Mono.just(Tuples.of(userResponse, loanTypeRegistered));
                                })
                )
                .flatMap(tuple -> {
                    User userResponse = tuple.getT1();
                    LoanType loanTypeRegistered = tuple.getT2();

                    loanApplication.setEmail(userResponse.getEmail());
                    loanApplication.setStatusId(1L);
                    loanApplication.setFullName(userResponse.getFirstName() + " " + userResponse.getLastName());
                    loanApplication.setBaseSalary(userResponse.getBaseSalary());
                    return loanApplicationRepository.registerLoanApplication(loanApplication)
                            .flatMap(registered -> validateDebtCapacity(
                                    registered,
                                    userResponse,
                                    loanTypeRegistered,
                                    idCard));
                })
                .switchIfEmpty(Mono.error(new SavingInDatabaseException(
                        500, "Error registering the request in the database")));
    }

    private Mono<LoanApplicationDto> validateDebtCapacity(
            LoanApplicationDto registered,
            User userResponse,
            LoanType loanTypeRegistered,
            String idCard) {
        // Verificamos si requiere validación automática
        if (Boolean.TRUE.equals(loanTypeRegistered.getAutomaticValidation())) {
            return loanApplicationRepository.listAllApprovedApplicationsOfUser(registered.getEmail())
                    .collectList()
                    .flatMap(applicationsApproved -> {
                        LoanApplicationCalculateCapacityDto newLoan = new LoanApplicationCalculateCapacityDto(
                                registered.getApplicationId(),
                                registered.getAmount(),
                                registered.getTerm(),
                                loanTypeRegistered.getInterestRate()
                        );

                        LoanValidationRequest validationRequest = LoanValidationRequest.builder()
                                .idCard(idCard)
                                .email(userResponse.getEmail())
                                .fullName(userResponse.getFirstName() + " " + userResponse.getLastName())
                                .baseSalary(userResponse.getBaseSalary())
                                .applicationsApproved(applicationsApproved)
                                .newLoan(newLoan)
                                .build();
                        // Enviamos el mensaje a la Lambda y devolvemos el préstamo registrado
                        return messageGateway.sendMessageToLambdaCalculateCapacity(validationRequest)
                                .thenReturn(registered);
                    });
        } else {
            return Mono.just(registered);
        }
    }
}

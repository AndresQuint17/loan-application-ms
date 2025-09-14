package co.com.loans.usecase.registerLoanApplication;

import co.com.loans.model.exceptions.LoanTypeDoesNotExistException;
import co.com.loans.model.exceptions.SavingInDatabaseException;
import co.com.loans.model.exceptions.UserDoesNotExistException;
import co.com.loans.model.loanapplication.LoanApplication;
import co.com.loans.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.loans.model.loanapplication.gateways.UserRestConsumerGateway;
import co.com.loans.model.loantype.LoanType;
import co.com.loans.model.loantype.gateways.LoanTypeRepository;
import co.com.loans.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class RegisterLoanApplicationUseCaseTest {

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @Mock
    private UserRestConsumerGateway restConsumer;

    @InjectMocks
    private RegisterLoanApplicationUseCase useCase;

    //Flujo exitoso
    @Test
    void shouldRegisterLoanApplicationSuccessfully() {

        String idCard = "12345678";
        String loanTypeName = "Personal Loan";

        LoanApplication loanApplication = LoanApplication.builder()
                .amount(5000.0)
                .term(24)
                .build();

        User user = new User("Sara",
                "White",
                "",
                LocalDate.of(1994,9,24),
                "",
                "",
                "sarawhite@mail.com",
                new BigDecimal(4500000));

        LoanType loanType = LoanType.builder().loanTypeId(1L).name(loanTypeName).build();


        Mockito.when(restConsumer.getUserByIdCard(idCard))
                .thenReturn(Mono.just(user));
        Mockito.when(loanTypeRepository.getLoanTypeByName(loanTypeName))
                .thenReturn(Mono.just(loanType));
        // Cuando se mande a guardar, se devuelve el mismo objeto
        Mockito.when(loanApplicationRepository.registerLoanApplication(Mockito.any(LoanApplication.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));


        StepVerifier.create(useCase.registerLoanApplication(idCard, loanTypeName, loanApplication))
                .expectNextMatches(loanApp ->
                        loanApp.getEmail().equals("sarawhite@mail.com") &&
                                loanApp.getLoanTypeId().equals(1L) &&
                                loanApp.getStatusId().equals(1L)
                )
                .verifyComplete();
    }

    //Tipo de prestamo no existe
    @Test
    void shouldFailWhenLoanTypeDoesNotExist() {

        String idCard = "12345678";
        String loanTypeName = "Unknown Loan";

        LoanApplication inputApp = LoanApplication.builder()
                .amount(5000.0)
                .term(24)
                .build();

        Mockito.when(restConsumer.getUserByIdCard(idCard))
                .thenReturn(Mono.just(new User("Unknown",
                        "User",
                        "",
                        LocalDate.of(1994,9,24),
                        "",
                        "",
                        "user@mail.com",
                        new BigDecimal(4500000))));

        Mockito.when(loanTypeRepository.getLoanTypeByName(loanTypeName))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.registerLoanApplication(idCard, loanTypeName, inputApp))
                .expectErrorMatches(throwable ->
                        throwable instanceof LoanTypeDoesNotExistException &&
                                throwable.getMessage().contains("Loan type not found"))
                .verify();
    }

    //User not found
    @Test
    void shouldFailWhenUserNotFound() {

        String idCard = "00000000";
        String loanTypeName = "Personal Loan";

        LoanApplication inputApp = LoanApplication.builder()
                .amount(5000.0)
                .term(24)
                .build();

        Mockito.when(restConsumer.getUserByIdCard(idCard))
                .thenReturn(Mono.error(new UserDoesNotExistException(404, "User does not exist")));


        StepVerifier.create(useCase.registerLoanApplication(idCard, loanTypeName, inputApp))
                .expectErrorSatisfies(throwable -> {
                    Assertions.assertInstanceOf(UserDoesNotExistException.class, throwable);
                    Assertions.assertEquals("User does not exist", throwable.getMessage());
                })
                .verify();
    }

    //No se pudo guardar en BD
    @Test
    void shouldFailWhenLoanApplicationCannotBeSaved() {

        String idCard = "12345678";
        String loanTypeName = "Personal Loan";

        LoanApplication loanApplication = LoanApplication.builder()
                .amount(5000.0)
                .term(24)
                .build();

        User user = new User("Sara",
                "White",
                "",
                LocalDate.of(1994,9,24),
                "",
                "",
                "sarawhite@mail.com",
                new BigDecimal(4500000));
        LoanType loanType = LoanType.builder().loanTypeId(1L).name(loanTypeName).build();

        Mockito.when(restConsumer.getUserByIdCard(idCard)).thenReturn(Mono.just(user));
        Mockito.when(loanTypeRepository.getLoanTypeByName(loanTypeName)).thenReturn(Mono.just(loanType));
        Mockito.when(loanApplicationRepository.registerLoanApplication(Mockito.any()))
                .thenReturn(Mono.error(new SavingInDatabaseException(500, "Database error")));


        StepVerifier.create(useCase.registerLoanApplication(idCard, loanTypeName, loanApplication))
                .expectErrorMatches(error -> error instanceof SavingInDatabaseException &&
                        error.getMessage().equals("Database error"))
                .verify();
    }


}

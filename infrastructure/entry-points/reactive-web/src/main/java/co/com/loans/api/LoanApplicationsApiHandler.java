package co.com.loans.api;

import co.com.loans.api.mapper.LoanMapper;
import co.com.loans.api.model.LoanApplicationRequest;
import co.com.loans.model.loanapplication.LoanApplication;
import co.com.loans.model.loanapplication.validation.LoanApplicationValidations;
import co.com.loans.model.loantype.LoanType;
import co.com.loans.model.loantype.validation.LoanTypeValidations;
import co.com.loans.model.user.User;
import co.com.loans.model.user.validation.UserValidations;
import co.com.loans.usecase.registerLoanApplication.RegisterLoanApplicationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoanApplicationsApiHandler {
    private final RegisterLoanApplicationUseCase registerLoanApplicationUseCase;
    private final LoanMapper loanMapper;

    public Mono<ServerResponse> submitLoanApplication(ServerRequest serverRequest) {
        log.info("Received a loan application submission request.");

        return serverRequest.bodyToMono(LoanApplicationRequest.class)
                .doOnNext(request -> log.debug("Parsed request body: {}", request))
                .flatMap(requestDto -> {
                    String userIdCard = validateIdCard(requestDto.idCard());
                    String loanTypeName = validateLoanTypeName(requestDto.loanType());
                    return Mono.just(requestDto)
                            .flatMap(dto -> {
                                LoanApplication loanApplication = loanMapper.toDomain(dto);
                                log.debug("Mapped LoanApplicationRequest to domain model: {}", loanApplication);
                                LoanApplicationValidations.validate(loanApplication);
                                log.info("Loan application passed domain validations.");
                                return registerLoanApplicationUseCase.registerLoanApplication(userIdCard, loanTypeName, loanApplication)
                                        .map(loanMapper::toLoanApplicationResponse);
                            });
                })
                .flatMap(response -> {
                    if (response.getStatus().equals("1")) {
                        response.setMessage("Pending approval");
                    }
                    log.info("Loan application completed with status: {}", response.getStatus());
                    return ServerResponse.ok().bodyValue(response);
                })
                .doFinally(signalType -> log.info("Loan application processing execution finished. Signal: {}", signalType));
    }

    private String validateIdCard(String idCard) {
        User user = new User();
        user.setIdCard(idCard);
        UserValidations.validate(user);
        return idCard;
    }

    private String validateLoanTypeName(String name) {
        LoanType loanType = new LoanType();
        loanType.setName(name);
        LoanTypeValidations.validate(loanType);
        return name.trim().toUpperCase(Locale.ROOT);
    }
}

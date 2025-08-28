package co.com.loans.api;

import co.com.loans.api.mapper.LoanMapper;
import co.com.loans.api.model.LoanApplicationRequest;
import co.com.loans.api.model.LoanApplicationResponse;
import co.com.loans.model.loanapplication.LoanApplication;
import co.com.loans.usecase.registerLoanApplication.RegisterLoanApplicationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@RequiredArgsConstructor
@Component
public class LoanApplicationsApiHandler {
    private final RegisterLoanApplicationUseCase registerLoanApplicationUseCase;
    private final LoanMapper loanMapper;

    public Mono<ServerResponse> submitLoanApplication(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoanApplicationRequest.class)
                .flatMap(requestDto -> {
                    String userIdCard = requestDto.idCard();
                    return Mono.just(requestDto)
                            .flatMap(dto -> {
                                LoanApplication loanApplication = loanMapper.toDomain(dto);
                                return registerLoanApplicationUseCase.registerLoanApplication(userIdCard, loanApplication)
                                        .map(loanMapper::toLoanApplicationResponse);
                            });
                })
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }
}

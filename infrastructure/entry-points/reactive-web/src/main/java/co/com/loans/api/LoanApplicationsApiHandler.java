package co.com.loans.api;

import co.com.loans.api.jwt.provider.JwtTokenProvider;
import co.com.loans.api.mapper.LoanMapper;
import co.com.loans.api.model.LoanApplicationRequest;
import co.com.loans.model.loanapplication.LoanApplication;
import co.com.loans.model.loanapplication.validation.LoanApplicationValidations;
import co.com.loans.usecase.listLoanApplications.ListLoanApplicationsUseCase;
import co.com.loans.usecase.registerLoanApplication.RegisterLoanApplicationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoanApplicationsApiHandler {
    private final RegisterLoanApplicationUseCase registerLoanApplicationUseCase;
    private final ListLoanApplicationsUseCase listLoanApplicationsUseCase;
    private final LoanMapper loanMapper;
    private final JwtTokenProvider tokenProvider;

    @PreAuthorize("hasRole('CLIENTE')")
    public Mono<ServerResponse> submitLoanApplication(ServerRequest serverRequest) {
        log.info("Received a loan application submission request.");

        return serverRequest.bodyToMono(LoanApplicationRequest.class)
                .doOnNext(request -> log.debug("Parsed request body: {}", request))
                .flatMap(requestDto -> {

                    String token = extractAuthorizationToken(serverRequest);

                    return Mono.just(requestDto)
                            .flatMap(dto -> {
                                LoanApplication loanApplication = loanMapper.toDomain(dto);
                                log.debug("Mapped LoanApplicationRequest to domain model: {}", loanApplication);
                                LoanApplicationValidations.validate(loanApplication);
                                log.info("Loan application passed domain validations.");
                                return registerLoanApplicationUseCase
                                        .registerLoanApplication(
                                                requestDto.idCard(),
                                                requestDto.loanType(),
                                                loanApplication,
                                                token,
                                                tokenProvider.getSubject(token)
                                        )
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

    @PreAuthorize("hasRole('ASESOR')")
    public Mono<ServerResponse> listLoanApplications(ServerRequest request) {
        log.info("Received a request to list loan applications.");

        Integer page = request.queryParam("page")
                .map(Integer::parseInt)
                .orElse(0);

        Integer size = request.queryParam("size")
                .map(Integer::parseInt)
                .orElse(20);

        log.debug("Listing applications with page: {} and size: {}", page, size);

        // 2. Usar un caso de uso para obtener las solicitudes de forma paginada.
        return listLoanApplicationsUseCase.listPendingReviewLoanApplications(page, size)
                .flatMap(loanApplicationsList -> {
                    // 3. Crear y devolver una respuesta exitosa con la lista de solicitudes.
                    return ServerResponse.ok().bodyValue(loanApplicationsList);
                })
                .doOnError(throwable -> log.error("Error listing loan applications", throwable))
                .doFinally(signalType -> log.info("Listing loan applications execution finished. Signal: {}", signalType));
    }

    private String extractAuthorizationToken(ServerRequest serverRequest) {
        return serverRequest.headers().header("Authorization").stream()
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Authorization token missing or malformed"));
    }
}

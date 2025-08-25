package co.com.loans.api;

import co.com.loans.api.model.LoanApplicationRequest;
import co.com.loans.api.model.LoanApplicationResponse;
import lombok.extern.log4j.Log4j2;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@AllArgsConstructor
@Component
public class LoanApplicationsApiHandler {
//    private final UseCase someUseCase;

    public Mono<ServerResponse> submitLoanApplication(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoanApplicationRequest.class)
                .flatMap(body -> submitLoanApplicationMock()) // TODO: Call real use case here -> someUseCase.some()
                .flatMap(response -> ServerResponse.ok().bodyValue(response)); // TODO: Customize response here
    }

    private Mono<LoanApplicationResponse> submitLoanApplicationMock() { // TODO: Remove this mock method
        return Mono.fromSupplier(() -> {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            try {
                return mapper.readValue("{\n  \"applicationId\" : \"applicationId\",\n  \"status\" : \"Pending Review\"\n}", LoanApplicationResponse.class);
            } catch (Exception e) {
                throw new RuntimeException("Cannot parse example to LoanApplicationResponse");
            }
        });
    }
}

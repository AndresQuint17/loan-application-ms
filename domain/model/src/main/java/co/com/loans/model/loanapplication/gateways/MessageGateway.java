package co.com.loans.model.loanapplication.gateways;

import co.com.loans.model.loanapplication.dto.LoanValidationRequest;
import reactor.core.publisher.Mono;

public interface MessageGateway {
    Mono<String> send(String message);
    Mono<String> sendMessageToLambdaCalculateCapacity(LoanValidationRequest bodyRequest);
}

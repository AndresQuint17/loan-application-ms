package co.com.loans.model.loanapplication.gateways;

import reactor.core.publisher.Mono;

public interface MessageGateway {
    Mono<String> send(String message);
}

package co.com.loans.consumer;

import co.com.loans.consumer.dto.ResponseUserByIdCardDto;
import co.com.loans.consumer.exceptions.UserServiceErrorHandler;
import co.com.loans.consumer.mapper.UserMapper;
import co.com.loans.model.exceptions.ConnectionFailedException;
import co.com.loans.model.exceptions.UserDoesNotExistException;
import co.com.loans.model.loanapplication.gateways.UserRestConsumerGateway;
import co.com.loans.model.user.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestConsumer /* implements Gateway from domain */ implements UserRestConsumerGateway {
    private final WebClient client;
    private final UserMapper userMapper;
    private final UserServiceErrorHandler errorHandler;

    @Override
    @CircuitBreaker(name = "getUserByIdCard")
    public Mono<User> getUserByIdCard(String idCard, String bearerToken) {
        return client
                .get()
                .uri("/api/v1/usuarios/{idCard}", idCard)
                .header("Authorization", "Bearer " + bearerToken)
                .retrieve()
                .bodyToMono(ResponseUserByIdCardDto.class)
                .map(userMapper::toDomain)
                .onErrorResume(ex -> errorHandler.handleError(ex, idCard));
    }
}

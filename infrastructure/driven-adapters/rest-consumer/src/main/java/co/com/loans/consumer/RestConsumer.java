package co.com.loans.consumer;

import co.com.loans.consumer.dto.ResponseUserByIdCardDto;
import co.com.loans.consumer.mapper.UserMapper;
import co.com.loans.model.loanapplication.gateways.UserRestConsumerGateway;
import co.com.loans.model.user.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer /* implements Gateway from domain */ implements UserRestConsumerGateway {
    private final WebClient client;
    private final UserMapper userMapper;
    private final Logger logger = LoggerFactory.getLogger(RestConsumer.class);

    @Override
    @CircuitBreaker(name = "getUserByIdCard")
    public Mono<User> getUserByIdCard(String idCard) {
        return client
                .get()
                .uri("/api/v1/usuarios/{idCard}", idCard)  // Utiliza el parámetro {idCard}
                .retrieve()
                .bodyToMono(ResponseUserByIdCardDto.class)  // Mapea la respuesta a UserResponse
                .map(userMapper::toDomain)
                .onErrorResume(e -> {
                    logger.error(e.getMessage());
                    // Maneja errores de la llamada (e.g., no encontrado o timeout)
                    return Mono.empty();  // Devuelve un Mono vacío
                });
    }
}

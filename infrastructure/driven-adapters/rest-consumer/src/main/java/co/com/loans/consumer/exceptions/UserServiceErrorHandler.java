package co.com.loans.consumer.exceptions;

import co.com.loans.model.exceptions.ConnectionFailedException;
import co.com.loans.model.exceptions.UnexpectedException;
import co.com.loans.model.exceptions.UserDoesNotExistException;
import co.com.loans.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class UserServiceErrorHandler {
    public Mono<User> handleError(Throwable ex, String idCard) {
        log.error("Error in UserService call: {}", ex.getMessage());

        if (ex instanceof WebClientRequestException) {
            return Mono.error(new ConnectionFailedException (
                    500, "Error trying to make a request to the auth service"));
        } else if (ex instanceof  WebClientResponseException) {
            return Mono.error(new UserDoesNotExistException (
                    404, "User with idCard " + idCard + " does not exist"));
        }

        return Mono.error(new UnexpectedException(
                500, "Unexpected exception occurred"
        ));
    }
}

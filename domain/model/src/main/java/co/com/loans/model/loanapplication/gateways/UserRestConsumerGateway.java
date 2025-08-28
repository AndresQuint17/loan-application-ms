package co.com.loans.model.loanapplication.gateways;

import co.com.loans.model.loanapplication.LoanApplication;
import co.com.loans.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRestConsumerGateway {
    Mono<User> getUserByIdCard(String idCard);
}

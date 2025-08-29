package co.com.loans.model.loantype.gateways;

import co.com.loans.model.loantype.LoanType;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<LoanType> getLoanTypeByName(String name);
}

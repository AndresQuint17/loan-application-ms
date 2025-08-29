package co.com.loans.r2dbc;

import co.com.loans.model.loanapplication.LoanApplication;
import co.com.loans.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.loans.r2dbc.entity.LoanApplicationEntity;
import co.com.loans.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class LoanApplicationRepositoryAdapter extends ReactiveAdapterOperations<
        LoanApplication/* change for domain model */,
        LoanApplicationEntity/* change for adapter model */,
        Long,
        LoanApplicationReactiveRepository
        > implements LoanApplicationRepository {
    public LoanApplicationRepositoryAdapter(LoanApplicationReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, domainEntity -> mapper.map(domainEntity, LoanApplication.class/* change for domain model */));
    }

    @Override
    public Mono<LoanApplication> registerLoanApplication(LoanApplication loanApplication) {
        return super.save(loanApplication);
    }
}

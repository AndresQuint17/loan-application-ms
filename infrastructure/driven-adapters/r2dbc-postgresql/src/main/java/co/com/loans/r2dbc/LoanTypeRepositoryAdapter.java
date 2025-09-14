package co.com.loans.r2dbc;

import co.com.loans.model.loantype.LoanType;
import co.com.loans.model.loantype.gateways.LoanTypeRepository;
import co.com.loans.r2dbc.entity.LoanTypeEntity;
import co.com.loans.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class LoanTypeRepositoryAdapter extends ReactiveAdapterOperations<
        LoanType/* change for domain model */,
        LoanTypeEntity/* change for adapter model */,
        Long,
        LoanTypeReactiveRepository
        > implements LoanTypeRepository {
    public LoanTypeRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, domainEntity -> mapper.map(domainEntity, LoanType.class));
    }

    @Override
    public Mono<LoanType> getLoanTypeByName(String name) {
        return repository.findByName(name).map(entity -> mapper.map(entity, LoanType.class));
    }
}

package co.com.loans.r2dbc;

import co.com.loans.model.loanapplication.LoanApplication;
import co.com.loans.model.loanapplication.dto.LoanApplicationDto;
import co.com.loans.model.loanapplication.dto.LoanApplicationsResponseDto;
import co.com.loans.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.loans.r2dbc.entity.LoanApplicationEntity;
import co.com.loans.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
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

    @Override
    public Mono<LoanApplicationsResponseDto> findPaginated(int page, int size) {
        // Identificadores de los estados a filtrar
        // 1 -> "Pending Review"
        // 3 -> "Rejected"
        // 5 ->	"Manual Review"
        List<Long> statusIds = Arrays.asList(1L, 3L, 5L);

        // Se usa PageRequest para la paginación. Aunque R2DBC no lo usa directamente en
        // la consulta nativa.
        PageRequest pageRequest = PageRequest.of(page, size);

        log.debug("1. Obtener el Flux de aplicaciones paginadas y filtradas");
        Flux<LoanApplicationDto> applicationsFlux = repository.findPendingReviewApplications(statusIds, pageRequest.getOffset(), pageRequest.getPageSize())
                .doOnNext(application -> log.info("Found pending application: {}", application))
                .doOnError(throwable -> log.error(throwable.getMessage()));
        log.debug("2. Obtener el Mono con el cálculo de la deuda total aprobada");
        Mono<BigDecimal> totalMonthlyDebtMono = repository.calculateTotalMonthlyApprovedDebt()
                .defaultIfEmpty(BigDecimal.ZERO);

        log.debug("3. Combinar ambos flujos reactivos en un solo Mono");
        return applicationsFlux.collectList()
                .zipWith(totalMonthlyDebtMono, (applicationsList, totalDebt) ->
                        new LoanApplicationsResponseDto(applicationsList, totalDebt)
                );
    }
}

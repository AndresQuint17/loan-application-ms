package co.com.loans.r2dbc;

import co.com.loans.model.loanapplication.dto.LoanApplicationDto;
import co.com.loans.r2dbc.entity.LoanApplicationEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

public interface LoanApplicationReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, Long>, ReactiveQueryByExampleExecutor<LoanApplicationEntity> {

    @Query("SELECT " +
            "a.application_id, a.amount, a.term, a.email, a.full_name, a.base_salary::numeric, " +
            "lt.name AS loan_type_name, lt.interest_rate, s.name AS status_name " +
            "FROM applications a " +
            "JOIN loan_types lt ON a.loan_type_id = lt.loan_type_id " +
            "JOIN statuses s ON a.status_id = s.status_id " +
            "WHERE a.status_id IN (:statusIds) " +
            "ORDER BY a.application_id " +
            "OFFSET :offset " +
            "LIMIT :size")
    Flux<LoanApplicationDto> findPendingReviewApplications(@Param("statusIds") List<Long> statusIds, @Param("offset") Long offset, @Param("size") int size);

    @Query("SELECT SUM(a.amount) FROM applications a WHERE a.status_id = 2")
    Mono<BigDecimal> calculateTotalMonthlyApprovedDebt();

    @Query("SELECT status_id FROM statuses WHERE name = :statusName")
    Mono<Long> findStatusIdByName(@Param("statusName") String statusName);
}

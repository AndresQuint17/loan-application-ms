package co.com.loans.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("loan_applications")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class LoanApplicationEntity {
    @Id
    @Column("application_id")
    private Long applicationId;
    @Column("amount")
    private Double amount;
    @Column("term")
    private Integer term;
    @Column("email")
    private String email;
    @Column("status_id")
    private Long statusId;
    @Column("status_id")
    private Long loanTypeId;
}

package co.com.loans.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("applications")
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
    @Column("loan_type_id")
    private Long loanTypeId;
    @Column("full_name")
    private String fullName;
    @Column("base_salary")
    private BigDecimal baseSalary;
}

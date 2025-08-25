package co.com.loans.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("loan_types")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class LoanTypeEntity {
    @Id
    @Column("loan_type_id")
    private Long loanTypeId;
    @Column("name")
    private String name;
    @Column("minimum_amount")
    private Double minimumAmount;
    @Column("maximum_amount")
    private Double maximumAmount;
    @Column("interest_rate")
    private Double interestRate;
    @Column("automatic_validation")
    private Boolean automaticValidation;
}

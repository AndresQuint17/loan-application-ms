package co.com.loans.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("statuses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class StatusEntity {
    @Id
    @Column("status_id")
    private Long statusId;
    @Column("name")
    private String name;
    @Column("description")
    private String description;
}

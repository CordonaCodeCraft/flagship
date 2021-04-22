package flagship.domain.cases.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Entity(name = "Warning")
@Table(name = "warnings")
public class Warning extends BaseEntity {

    private String dueType;
    private LocalDate warningDate;
    private BigDecimal warningCoefficient;
}

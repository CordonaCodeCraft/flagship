package flagship.domain.cases.entities;

import flagship.domain.tariffs.PdaWarningsGenerator;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static flagship.domain.tariffs.PdaWarningsGenerator.*;
import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Entity(name = "Warning")
@Table(name = "warnings")
public class Warning extends BaseEntity {

  @Enumerated(value = STRING)
  private DueType dueType;

  @Enumerated(value = STRING)
  private PdaWarning warningType;

  private LocalDate warningDate;

  private BigDecimal warningCoefficient;
}

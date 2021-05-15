package flagship.domain.cases.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static flagship.domain.PdaWarningsGenerator.DueType;
import static flagship.domain.PdaWarningsGenerator.WarningType;
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
  private WarningType warningType;

  private LocalDate warningDate;

  private BigDecimal warningFactor;

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    if (!super.equals(other)) return false;

    final Warning warning = (Warning) other;

    if (dueType != warning.dueType) return false;
    if (warningType != warning.warningType) return false;
    if (!Objects.equals(warningDate, warning.warningDate)) return false;
    return warningFactor.equals(warning.warningFactor);
  }
}

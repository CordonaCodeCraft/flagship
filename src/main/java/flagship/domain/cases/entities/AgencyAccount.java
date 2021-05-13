package flagship.domain.cases.entities;

import lombok.*;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class AgencyAccount {

  private BigDecimal basicAgencyDue;
  private BigDecimal clearanceDue;
  private BigDecimal carsDue;
  private BigDecimal communicationsDue;
  private BigDecimal bankExpensesDue;
  private BigDecimal overtimeDue;
}

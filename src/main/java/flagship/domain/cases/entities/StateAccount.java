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
public class StateAccount {

  private BigDecimal tonnageDue;
  private BigDecimal wharfDue;
  private BigDecimal canalDue;
  private BigDecimal lightDue;
  private BigDecimal marpolDue;
  private BigDecimal mooringDue;
  private BigDecimal boomContainmentDue;
  private BigDecimal sailingPermissionDue;
}

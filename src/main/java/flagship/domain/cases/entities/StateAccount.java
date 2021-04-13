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
  private BigDecimal sailingPermissionDue;
  private BigDecimal pilotageDue;
  private BigDecimal tugServiceDue;
  private BigDecimal mooringAndUnmooringDue;
  private BigDecimal marpolDue;
}

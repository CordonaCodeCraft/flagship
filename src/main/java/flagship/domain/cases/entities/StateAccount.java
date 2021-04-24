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
  //todo: fixed price for sailing permission, no calculator implemented
  private BigDecimal sailingPermissionDue;
  private BigDecimal mooringAndUnmooringDue;
  private BigDecimal marpolDue;
}

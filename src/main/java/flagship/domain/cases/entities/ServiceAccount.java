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
public class ServiceAccount {

  // todo: When those are being calculated - multiply by two - for arrival and departure
  private BigDecimal pilotageServiceDue;
  private BigDecimal tugServiceDue;
}

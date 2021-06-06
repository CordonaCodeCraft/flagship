package flagship.domain.cases.dto;

import flagship.domain.cases.entities.Ship;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdaShip {

  private String name;
  private Ship.ShipType type;
  private BigDecimal lengthOverall;
  private BigDecimal grossTonnage;
  private Boolean hasIncreasedManeuverability;
}

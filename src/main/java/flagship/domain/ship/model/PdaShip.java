package flagship.domain.ship.model;

import flagship.domain.ship.entity.Ship;
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

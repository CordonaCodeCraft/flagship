package flagship.domain.ship.model;

import lombok.*;

import java.math.BigDecimal;

import static flagship.domain.ship.entity.Ship.ShipType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdaShip {

  private String name;
  private ShipType type;
  private BigDecimal lengthOverall;
  private BigDecimal grossTonnage;
  private Boolean hasIncreasedManeuverability;
}

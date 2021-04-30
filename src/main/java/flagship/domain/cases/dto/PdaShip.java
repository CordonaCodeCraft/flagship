package flagship.domain.cases.dto;

import flagship.domain.cases.entities.enums.ShipType;
import lombok.*;

import java.math.BigDecimal;

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
  private Boolean requiresSpecialPilot;
  private Boolean hasIncreasedManeuverability;
  private Boolean transportsDangerousCargo;
}

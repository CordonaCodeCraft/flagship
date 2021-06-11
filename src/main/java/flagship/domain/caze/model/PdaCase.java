package flagship.domain.caze.model;

import flagship.domain.pda.entity.ProformaDisbursementAccount;
import flagship.domain.port.model.PdaPort;
import flagship.domain.ship.model.PdaShip;
import flagship.domain.warning.entity.Warning;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static flagship.domain.caze.entity.Case.CallPurpose;
import static flagship.domain.warning.generator.WarningsGenerator.WarningType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class PdaCase {

  private PdaShip ship;
  private PdaPort port;
  private Set<WarningType> warningTypes;
  private Set<Warning> warnings;
  private List<String> cargoManifest;
  private CallPurpose callPurpose;
  private Integer callCount;
  private Integer alongsideDaysExpected;
  private LocalDate estimatedDateOfArrival;
  private LocalDate estimatedDateOfDeparture;
  private Boolean arrivesFromBulgarianPort;
  private BigDecimal clientDiscountCoefficient;
  private ProformaDisbursementAccount proformaDisbursementAccount;
}

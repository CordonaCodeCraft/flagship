package flagship.bootstrap.initializers;

import flagship.domain.calculation.tariffs.state.SailingPermissionTariff;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class SailingPermissionDueTariffInitializer extends Initializer {

  public static SailingPermissionTariff getTariff() {

    final SailingPermissionTariff sailingPermissionTariff = new SailingPermissionTariff();

    sailingPermissionTariff.setSailingPermissionDue(BigDecimal.valueOf(50.00));

    log.info("Sailing permission due tariff initialized");

    return sailingPermissionTariff;
  }
}

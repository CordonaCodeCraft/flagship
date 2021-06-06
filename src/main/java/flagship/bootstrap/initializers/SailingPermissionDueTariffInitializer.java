package flagship.bootstrap.initializers;

import flagship.domain.calculation.tariffs.state.SailingPermissionTariff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class SailingPermissionDueTariffInitializer {

  public static SailingPermissionTariff getTariff() {

    final SailingPermissionTariff sailingPermissionTariff = new SailingPermissionTariff();

    sailingPermissionTariff.setSailingPermissionDue(BigDecimal.valueOf(50.00));

    log.info("Sailing permission due tariff initialized");

    return sailingPermissionTariff;
  }
}

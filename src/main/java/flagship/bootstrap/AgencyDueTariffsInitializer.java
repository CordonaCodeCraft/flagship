package flagship.bootstrap;

import flagship.domain.tariffs.Due;
import flagship.domain.tariffs.GtRange;
import flagship.domain.tariffs.agencyduestariffs.BasicAgencyDueTariff;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class AgencyDueTariffsInitializer {

  public static void initializeTariffs(final BasicAgencyDueTariff basicAgencyDueTariff) {
    initializeBasicAgencyDueTariff(basicAgencyDueTariff);
  }

  private static void initializeBasicAgencyDueTariff(
      final BasicAgencyDueTariff basicAgencyDueTariff) {

    Map<GtRange, Due> basicAgencyDuePerGT = new LinkedHashMap<>();
    basicAgencyDuePerGT.put(new GtRange(150, 1000), new Due(BigDecimal.valueOf(505.00)));
    basicAgencyDuePerGT.put(new GtRange(1001, 2000), new Due(BigDecimal.valueOf(610.00)));
    basicAgencyDuePerGT.put(new GtRange(2001, 3000), new Due(BigDecimal.valueOf(715.00)));
    basicAgencyDuePerGT.put(new GtRange(3001, 4000), new Due(BigDecimal.valueOf(835.00)));
    basicAgencyDuePerGT.put(new GtRange(4001, 5000), new Due(BigDecimal.valueOf(955.00)));
    basicAgencyDuePerGT.put(new GtRange(5001, 6000), new Due(BigDecimal.valueOf(1090.00)));
    basicAgencyDuePerGT.put(new GtRange(6001, 7000), new Due(BigDecimal.valueOf(1225.00)));
    basicAgencyDuePerGT.put(new GtRange(7001, 8000), new Due(BigDecimal.valueOf(1375.00)));
    basicAgencyDuePerGT.put(new GtRange(8001, 9000), new Due(BigDecimal.valueOf(1525.00)));
    basicAgencyDuePerGT.put(new GtRange(9001, 10000), new Due(BigDecimal.valueOf(1690.00)));
    basicAgencyDuePerGT.put(
        new GtRange(10001, 650000), new Due(BigDecimal.valueOf(1690.00), BigDecimal.valueOf(75)));

    basicAgencyDueTariff.setBasicAgencyDuePerGrossTonnage(
        Collections.unmodifiableMap(basicAgencyDuePerGT));
    basicAgencyDueTariff.setDiscountCoefficientForCallPurpose(BigDecimal.valueOf(0.5));
    basicAgencyDueTariff.setGrossTonnageThreshold(BigDecimal.valueOf(10000.00));
  }
}

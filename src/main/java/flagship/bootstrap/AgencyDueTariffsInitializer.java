package flagship.bootstrap;

import flagship.domain.tariffs.AgencyDuesTariff;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.PortName;
import flagship.domain.tariffs.mix.Range;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static flagship.domain.tariffs.mix.PortName.*;

@Component
public class AgencyDueTariffsInitializer {

  public static void initializeTariffs(final AgencyDuesTariff basicAgencyDueTariff) {
    initializeBasicAgencyDueTariff(basicAgencyDueTariff);
  }

  private static void initializeBasicAgencyDueTariff(final AgencyDuesTariff agencyDuesTariff) {

    Map<Range, Due> basicAgencyDuePerGT = new LinkedHashMap<>();
    basicAgencyDuePerGT.put(new Range(150, 1000), new Due(505.00));
    basicAgencyDuePerGT.put(new Range(1001, 2000), new Due(610.00));
    basicAgencyDuePerGT.put(new Range(2001, 3000), new Due(715.00));
    basicAgencyDuePerGT.put(new Range(3001, 4000), new Due(835.00));
    basicAgencyDuePerGT.put(new Range(4001, 5000), new Due(955.00));
    basicAgencyDuePerGT.put(new Range(5001, 6000), new Due(1090.00));
    basicAgencyDuePerGT.put(new Range(6001, 7000), new Due(1225.00));
    basicAgencyDuePerGT.put(new Range(7001, 8000), new Due(1375.00));
    basicAgencyDuePerGT.put(new Range(8001, 9000), new Due(1525.00));
    basicAgencyDuePerGT.put(new Range(9001, 10000), new Due(1690.00));
    basicAgencyDuePerGT.put(new Range(10001, 650000), new Due(1690.00, 75.00));

    Map<Range, Map<Range, Due>> carsDueByGrossTonnageAndAlongsideDaysExpected =
        new LinkedHashMap<>();

    Map<Range, Due> first = new LinkedHashMap<>();
    first.put(new Range(1, 1), new Due(80.00));
    first.put(new Range(1, 5), new Due(120.00));
    first.put(new Range(6, 10), new Due(180.00));
    first.put(new Range(11, 20), new Due(250.00));

    Map<Range, Due> second = new LinkedHashMap<>();
    second.put(new Range(1, 1), new Due(110.00));
    second.put(new Range(1, 5), new Due(150.00));
    second.put(new Range(6, 10), new Due(200.00));
    second.put(new Range(11, 20), new Due(300.00));

    Map<Range, Due> third = new LinkedHashMap<>();
    third.put(new Range(1, 1), new Due(150.00));
    third.put(new Range(1, 5), new Due(230.00));
    third.put(new Range(6, 10), new Due(300.00));
    third.put(new Range(11, 20), new Due(350.00));

    carsDueByGrossTonnageAndAlongsideDaysExpected.put(new Range(150, 5999), first);
    carsDueByGrossTonnageAndAlongsideDaysExpected.put(new Range(6000, 14999), second);
    carsDueByGrossTonnageAndAlongsideDaysExpected.put(new Range(15000, 650000), third);

    Map<PortName, BigDecimal> carsDuesIncreaseCoefficientByPortName = new EnumMap<>(PortName.class);
    carsDuesIncreaseCoefficientByPortName.put(LESPORT, BigDecimal.valueOf(0.3));
    carsDuesIncreaseCoefficientByPortName.put(SRY_DOLPHIN, BigDecimal.valueOf(0.3));
    carsDuesIncreaseCoefficientByPortName.put(TEREM_FA, BigDecimal.valueOf(0.3));
    carsDuesIncreaseCoefficientByPortName.put(BOURGAS_WEST_TERMINAL, BigDecimal.valueOf(0.3));
    carsDuesIncreaseCoefficientByPortName.put(VARNA_WEST, BigDecimal.valueOf(0.5));
    carsDuesIncreaseCoefficientByPortName.put(FERRY_COMPLEX, BigDecimal.valueOf(0.5));
    carsDuesIncreaseCoefficientByPortName.put(PORT_ROSENETZ, BigDecimal.valueOf(0.5));
    carsDuesIncreaseCoefficientByPortName.put(BALCHIK_PORT, BigDecimal.valueOf(1));

    agencyDuesTariff.setBasicAgencyDuePerGrossTonnage(
        Collections.unmodifiableMap(basicAgencyDuePerGT));
    agencyDuesTariff.setCarsDueByGrossTonnageAndAlongsideDaysExpected(
        Collections.unmodifiableMap(carsDueByGrossTonnageAndAlongsideDaysExpected));
    agencyDuesTariff.setCarsDuesIncreaseCoefficientByPortName(
        Collections.unmodifiableMap(carsDuesIncreaseCoefficientByPortName));
    agencyDuesTariff.setBasicAgencyDueDiscountCoefficientByCallPurpose(BigDecimal.valueOf(0.5));
    agencyDuesTariff.setBasicAgencyDueGrossTonnageThreshold(BigDecimal.valueOf(10000.00));
    agencyDuesTariff.setClearanceIn(BigDecimal.valueOf(60.00));
    agencyDuesTariff.setClearanceOut(BigDecimal.valueOf(60.00));
    agencyDuesTariff.setBaseCommunicationsDue(BigDecimal.valueOf(100.00));
    agencyDuesTariff.setCommunicationsDueGrossTonnageThreshold(BigDecimal.valueOf(1000.00));
    agencyDuesTariff.setCommunicationsAdditionalDue(BigDecimal.valueOf(10.00));
    agencyDuesTariff.setBankExpensesCoefficient(BigDecimal.valueOf(0.5));
    agencyDuesTariff.setOvertimeCoefficient(BigDecimal.valueOf(0.2));
  }
}

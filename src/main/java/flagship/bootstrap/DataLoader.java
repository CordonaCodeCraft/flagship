package flagship.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.entities.Case;
import flagship.domain.entities.Port;
import flagship.domain.entities.Ship;
import flagship.domain.entities.enums.CallPurpose;
import flagship.domain.entities.enums.PortArea;
import flagship.domain.entities.enums.ShipType;
import flagship.persistence.services.ShipService;
import flagship.utils.calculators.CanalDueCalculator;
import flagship.utils.tariffs.CanalDueTariff;
import flagship.utils.tariffs.TonnageDueTariff;
import flagship.utils.tariffs.WharfDueTariff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static flagship.domain.entities.enums.CallPurpose.*;
import static flagship.domain.entities.enums.PortArea.*;
import static flagship.domain.entities.enums.ShipType.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements ApplicationRunner {

    private final ShipService shipService;
    private final ObjectMapper objectMapper;
    private final TonnageDueTariff tonnageDueTariff;
    private final WharfDueTariff wharfDueTariff;
    private final CanalDueTariff canalDueTariff;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        initializeTonnageDueTariff();
        initializeWharfDueTariff();
        initializeCanalDueTariff();

        produceTonnageDueJsonFile();

        File file = new File("src/main/resources/tonnageDueTariff.json");
        TonnageDueTariff tonnageDueTariff2 = objectMapper.readValue(file, TonnageDueTariff.class);

        Ship ship = Ship
                .builder()
                .lengthOverall(99.99)
                .grossTonnage(12345)
                .type(PASSENGER)
                .build();

        Port port = Port
                .builder()
                .area(FIRST)
                .build();

        Case activeCase = Case
                .builder()
                .ship(ship)
                .callPurpose(SPECIAL_PURPOSE_PORT_VISIT)
                .port(port)
                .callCount(2)
                .alongsideDaysExpected(3)
                .build();

        BigDecimal canalDue = CanalDueCalculator.calculateCanalDue(activeCase,canalDueTariff);
    }

    private void initializeTonnageDueTariff() {

        Map<ShipType, Double> tonnageDuesByShipType = new HashMap<>();
        tonnageDuesByShipType.put(OIL_TANKER, 0.50);
        tonnageDuesByShipType.put(RECREATIONAL, 0.10);
        tonnageDuesByShipType.put(MILITARY, 0.25);
        tonnageDuesByShipType.put(SPECIAL, 0.50);
        tonnageDueTariff.setTonnageDuesByShipType(tonnageDuesByShipType);

        Map<CallPurpose, Double> tonnageDuesByCallPurpose = new HashMap<>();
        tonnageDuesByCallPurpose.put(SPECIAL_PURPOSE_PORT_VISIT, 0.05);
        tonnageDueTariff.setTonnageDuesByCallPurpose(tonnageDuesByCallPurpose);

        Map<PortArea, Double> tonnageDuesByPortArea = new HashMap<>();
        tonnageDuesByPortArea.put(FIRST, 0.55);
        tonnageDuesByPortArea.put(SECOND, 0.40);
        tonnageDuesByPortArea.put(THIRD, 0.55);
        tonnageDuesByPortArea.put(FOURTH, 0.55);
        tonnageDueTariff.setTonnageDuesByPortArea(tonnageDuesByPortArea);

        Map<ShipType, Double> discountCoefficientByShipType = new HashMap<>();
        discountCoefficientByShipType.put(REEFER, 0.6);
        discountCoefficientByShipType.put(CONTAINER, 0.6);
        discountCoefficientByShipType.put(PASSENGER, 0.4);
        tonnageDueTariff.setDiscountCoefficientsByShipType(discountCoefficientByShipType);

        Map<CallPurpose, Double> discountCoefficientsByCallPurpose = new HashMap<>();
        discountCoefficientsByCallPurpose.put(RESUPPLY, 0.65);
        discountCoefficientsByCallPurpose.put(RECRUITMENT, 0.65);
        discountCoefficientsByCallPurpose.put(POSTAL, 0.65);
        discountCoefficientsByCallPurpose.put(REPAIR, 0.65);
        tonnageDueTariff.setDiscountCoefficientsByCallPurpose(discountCoefficientsByCallPurpose);

        tonnageDueTariff.setCallCountThreshold(4);
        tonnageDueTariff.setCallCountDiscountCoefficient(0.7);
    }

    private void initializeWharfDueTariff() {

        Map<CallPurpose, Double> discountCoefficientsByCallPurpose = new HashMap<>();
        discountCoefficientsByCallPurpose.put(RESUPPLY, 0.5);
        discountCoefficientsByCallPurpose.put(RECRUITMENT, 0.5);
        discountCoefficientsByCallPurpose.put(POSTAL, 0.5);
        discountCoefficientsByCallPurpose.put(REPAIR, 0.5);
        wharfDueTariff.setDiscountCoefficientsByCallPurpose(discountCoefficientsByCallPurpose);

        Set<ShipType> shipTypesNotEligibleForDiscount = new HashSet<>();
        shipTypesNotEligibleForDiscount.add(MILITARY);
        wharfDueTariff.setShipTypesNotEligibleForDiscount(shipTypesNotEligibleForDiscount);

        Map<ShipType, Double> wharfDuesByShipType = new HashMap<>();
        wharfDuesByShipType.put(MILITARY, 0.5);
        wharfDueTariff.setWharfDuesByShipType(wharfDuesByShipType);

        wharfDueTariff.setDefaultWharfDue(0.10);
    }

    private void initializeCanalDueTariff() {

        Map<PortArea, Double> canalDuesByPortArea = new HashMap<>();
        canalDuesByPortArea.put(FIRST, 0.04);
        canalDuesByPortArea.put(SECOND, 0.13);
        canalDuesByPortArea.put(THIRD, 0.04);
        canalDuesByPortArea.put(FOURTH, 0.07);
        canalDueTariff.setCanalDuesByPortArea(canalDuesByPortArea);

        Map<ShipType, Double> discountCoefficientByShipType = new HashMap<>();
        discountCoefficientByShipType.put(PASSENGER, 0.5);
        canalDueTariff.setDiscountCoefficientByShipType(discountCoefficientByShipType) ;

        Map<PortArea, Double> discountCoefficientsByPortAreaForContainers = new HashMap<>();
        discountCoefficientsByPortAreaForContainers.put(FIRST, 0.25);
        discountCoefficientsByPortAreaForContainers.put(SECOND, 0.74);
        discountCoefficientsByPortAreaForContainers.put(THIRD, 0.25);
        discountCoefficientsByPortAreaForContainers.put(FOURTH, 0.74);
        canalDueTariff.setDiscountCoefficientsByPortAreaForContainers(discountCoefficientsByPortAreaForContainers);

        Map<PortArea, Double> discountCoefficientsByPortAreaPerCallCountForContainers = new HashMap<>();
        discountCoefficientsByPortAreaPerCallCountForContainers.put(FIRST, 0.20);
        discountCoefficientsByPortAreaPerCallCountForContainers.put(SECOND, 0.59);
        discountCoefficientsByPortAreaPerCallCountForContainers.put(THIRD, 0.20);
        discountCoefficientsByPortAreaPerCallCountForContainers.put(FOURTH, 0.59);
        canalDueTariff.setDiscountCoefficientsByPortAreaPerCallCountForContainers(discountCoefficientsByPortAreaPerCallCountForContainers);

        canalDueTariff.setCallCountThreshold(3);
        canalDueTariff.setDefaultCallCountDiscountCoefficient(0.8);

    }

    private void produceTonnageDueJsonFile() throws IOException {

        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(Paths.get("src/main/resources/tonnageDueTariff.json").toFile(), tonnageDueTariff);

    }
}

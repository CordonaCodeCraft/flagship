package flagship.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.entities.Ship;
import flagship.domain.entities.enums.CallPurpose;
import flagship.domain.entities.enums.PortArea;
import flagship.domain.entities.enums.ShipType;
import flagship.persistence.services.ShipService;
import flagship.utils.tariffs.TonnageDueTariff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

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

    @Override
    public void run(ApplicationArguments args) throws Exception {

        initializeTonnageDueTariff();

        produceTonnageDueJsonFile();

        Ship ship = Ship
                .builder()
                .lengthOverall(99.99)
                .grossTonnage(12345)
                .type(ShipType.OIL_TANKER)
                .build();

        File file = new File("src/main/resources/tonnageDueTariff.json");
        TonnageDueTariff tonnageDueTariff2 = objectMapper.readValue(file, TonnageDueTariff.class);

        System.out.println();


//
//        Port port = Port
//                .builder()
//                .area(FIRST)
//                .build();
//
//        Case activeCase = Case
//                .builder()
//                .ship(ship)
//                .callPurpose(LOADING)
//                .port(port)
//                .callCount(1)
//                .alongsideDaysExpected(3)
//                .build();
//
//        BigDecimal tonnageDue = TonnageDueCalculator.calculateTonnageDue(activeCase, tonnageDueTariff);
//        BigDecimal wharfDue = WharfDueCalculator.calculateWharfDue(activeCase);
//


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

        List<CallPurpose> callPurposesEligibleForDiscount = Arrays.asList(RESUPPLY, RECRUITMENT, POSTAL, REPAIR);
        tonnageDueTariff.setCallPurposesEligibleForDiscount(callPurposesEligibleForDiscount);

        tonnageDueTariff.setCallCountThreshold(4);
        tonnageDueTariff.setCallCountDiscountCoefficient(0.7);
        tonnageDueTariff.setCallPurposeDiscountCoefficient(0.65);
    }

    private void produceTonnageDueJsonFile() throws IOException {

        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(Paths.get("src/main/resources/tonnageDueTariff.json").toFile(), tonnageDueTariff);

    }
}

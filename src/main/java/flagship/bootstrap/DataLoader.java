package flagship.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.entities.Case;
import flagship.domain.entities.Port;
import flagship.domain.entities.Ship;
import flagship.persistence.services.ShipService;
import flagship.utils.calculators.CanalDueCalculator;
import flagship.utils.calculators.LightDueCalculator;
import flagship.utils.tariffs.CanalDueTariff;
import flagship.utils.tariffs.LightDueTariff;
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

import static flagship.domain.entities.enums.CallPurpose.SPECIAL_PURPOSE_PORT_VISIT;
import static flagship.domain.entities.enums.PortArea.FIRST;
import static flagship.domain.entities.enums.ShipType.GENERAL;
import static flagship.domain.entities.enums.ShipType.PASSENGER;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements ApplicationRunner {

    private final ObjectMapper objectMapper;
    private final TonnageDueTariff tonnageDueTariff;
    private final WharfDueTariff wharfDueTariff;
    private final CanalDueTariff canalDueTariff;
    private final LightDueTariff lightDueTariff;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        TariffInitializer.initializeTariffs(tonnageDueTariff, wharfDueTariff, canalDueTariff, lightDueTariff);



//        Integer value = pairs
//                .entrySet()
//                .stream()
//                .filter(p -> target >= p.getKey().getValue0() && target <= p.getKey().getValue1())
//                .map(Map.Entry::getValue)
//                .findFirst().orElse(150);


        produceTonnageDueJsonFile();

        File file = new File("src/main/resources/tonnageDueTariff.json");
        TonnageDueTariff tonnageDueTariff2 = objectMapper.readValue(file, TonnageDueTariff.class);

        Ship ship = Ship
                .builder()
                .lengthOverall(99.99)
                .grossTonnage(10001)
                .type(GENERAL)
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
                .callCount(1)
                .alongsideDaysExpected(3)
                .build();

        BigDecimal canalDue = LightDueCalculator.calculateLightDue(activeCase, lightDueTariff);
    }


    private void produceTonnageDueJsonFile() throws IOException {

        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(Paths.get("src/main/resources/tonnageDueTariff.json").toFile(), tonnageDueTariff);

    }


}

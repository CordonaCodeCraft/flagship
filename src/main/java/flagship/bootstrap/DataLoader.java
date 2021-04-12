package flagship.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.entities.Case;
import flagship.domain.entities.Port;
import flagship.domain.entities.Ship;
import flagship.utils.calculators.state_dues_calulators.TonnageDueCalculator;
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
import java.nio.file.Paths;

import static flagship.domain.entities.enums.CallPurpose.*;
import static flagship.domain.entities.enums.PortArea.FIRST;
import static flagship.domain.entities.enums.ShipType.*;

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

        produceTonnageDueJsonFile();

        File file = new File("src/main/resources/tonnageDueTariff.json");
        TonnageDueTariff tonnageDueTariff2 = objectMapper.readValue(file, TonnageDueTariff.class);

        Ship ship = Ship
                .builder()
                .lengthOverall(99.99)
                .grossTonnage(10001)
                .type(REEFER)
                .build();

        Port port = Port
                .builder()
                .area(FIRST)
                .build();

        Case activeCase = Case
                .builder()
                .ship(ship)
                .callPurpose(RECRUITMENT)
                .port(port)
                .callCount(5)
                .alongsideDaysExpected(3)
                .build();

        TonnageDueCalculator tonnageDueCalculator = new TonnageDueCalculator();
        tonnageDueCalculator.calculate(activeCase, tonnageDueTariff);


    }


    private void produceTonnageDueJsonFile() throws IOException {

        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(Paths.get("src/main/resources/tonnageDueTariff.json").toFile(), tonnageDueTariff);

    }


}

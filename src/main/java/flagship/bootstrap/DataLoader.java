package flagship.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Port;
import flagship.domain.cases.entities.Ship;
import flagship.domain.utils.calculators.statedues.TonnageDueCalculator;
import flagship.domain.utils.tariffs.CanalDueTariff;
import flagship.domain.utils.tariffs.LightDueTariff;
import flagship.domain.utils.tariffs.TonnageDueTariff;
import flagship.domain.utils.tariffs.WharfDueTariff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static flagship.domain.cases.entities.enums.CallPurpose.RECRUITMENT;
import static flagship.domain.cases.entities.enums.PortArea.FIRST;
import static flagship.domain.cases.entities.enums.ShipType.REEFER;

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
        System.out.println();

        produceTonnageDueJson();

        Ship ship = Ship
                .builder()
                .lengthOverall(99.99)
                .grossTonnage(10001)
                .type(REEFER).build();

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

    private void produceTonnageDueJson() throws IOException {

        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(Paths.get("src/main/resources/tonnageDueTariff.json").toFile(), tonnageDueTariff);

        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(Paths.get("src/main/resources/wharfDueTariff.json").toFile(), wharfDueTariff);

        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(Paths.get("src/main/resources/lightDueTariff.json").toFile(), lightDueTariff);

        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(Paths.get("src/main/resources/canalDueTariff.json").toFile(), canalDueTariff);
    }
}

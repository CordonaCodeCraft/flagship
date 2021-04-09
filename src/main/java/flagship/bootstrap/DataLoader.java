package flagship.bootstrap;

import flagship.domain.entities.Case;
import flagship.domain.entities.Port;
import flagship.domain.entities.Ship;
import flagship.persistence.services.ShipService;
import flagship.utils.calculators.TonnageDueCalculator;
import flagship.utils.calculators.WharfDueCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static flagship.domain.entities.enums.CallPurpose.LOADING;
import static flagship.domain.entities.enums.CallPurpose.RECRUITMENT;
import static flagship.domain.entities.enums.PortArea.FIRST;
import static flagship.domain.entities.enums.ShipType.MILITARY;
import static flagship.domain.entities.enums.ShipType.OIL_TANKER;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements ApplicationRunner {

    private final ShipService shipService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Ship ship = Ship
                .builder()
                .lengthOverall(99.99)
                .grossTonnage(12345)
                .type(OIL_TANKER)
                .build();

        Port port = Port
                .builder()
                .area(FIRST)
                .build();

        Case activeCase = Case
                .builder()
                .ship(ship)
                .callPurpose(LOADING)
                .port(port)
                .callCount(1)
                .alongsideDaysExpected(3)
                .build();

        BigDecimal tonnageDue = TonnageDueCalculator.calculateTonnageDue(activeCase);
        BigDecimal wharfDue = WharfDueCalculator.calculateWharfDue(activeCase);


    }
}

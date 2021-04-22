package flagship.domain.utils.tariffs.serviceduestariffs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Component
public class HolidayCalendar {

    private Set<LocalDate> holidayCalendar;

}

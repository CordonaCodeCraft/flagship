package flagship.domain.calculation.tariffs.calendar;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class HolidayCalendar {

  private Set<LocalDate> holidayCalendar;
}

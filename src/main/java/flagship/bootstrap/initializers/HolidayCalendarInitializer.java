package flagship.bootstrap.initializers;

import flagship.domain.calculation.tariffs.calendar.HolidayCalendar;
import flagship.domain.calculation.tariffs.calendar.HolidayCalendarResolver;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import static java.time.Month.*;

@PropertySource("classpath:application.yml")
@Component
@NoArgsConstructor
@Slf4j
public class HolidayCalendarInitializer extends Initializer {

  // todo: find why does not detect externalized value
  @Value("${flagship.easter-day-of-month}")
  private int easterDayOfMonth;

  public HolidayCalendar initializeCalendar() {

    final int year = LocalDate.now().getYear();

    final LocalDate easter = LocalDate.of(LocalDate.now().getYear(), MAY, 2);

    final Set<LocalDate> officialHolidays = new TreeSet<>();

    officialHolidays.add(easter);

    officialHolidays.add(LocalDate.of(year, JANUARY, 1));
    officialHolidays.add(LocalDate.of(year, MARCH, 3));
    officialHolidays.add(LocalDate.of(year, APRIL, 30));
    officialHolidays.add(LocalDate.of(year, MAY, 1));
    officialHolidays.add(LocalDate.of(year, MAY, 6));
    officialHolidays.add(LocalDate.of(year, MAY, 24));
    officialHolidays.add(LocalDate.of(year, SEPTEMBER, 6));
    officialHolidays.add(LocalDate.of(year, SEPTEMBER, 22));
    officialHolidays.add(LocalDate.of(year, DECEMBER, 24));
    officialHolidays.add(LocalDate.of(year, DECEMBER, 25));
    officialHolidays.add(LocalDate.of(year, DECEMBER, 26));

    final HolidayCalendar holidayCalendar = new HolidayCalendar();

    holidayCalendar.setHolidayCalendar(HolidayCalendarResolver.resolve(officialHolidays));

    log.info("Holiday calendar initialized");

    return holidayCalendar;
  }
}

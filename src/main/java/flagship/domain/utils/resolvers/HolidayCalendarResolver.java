package flagship.domain.utils.resolvers;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.Month.*;

public abstract class HolidayCalendarResolver {

    public static Set<LocalDate> resolveWithDateFor(LocalDate easter) {

        Set<LocalDate> officialHolidays = new TreeSet<>();

        int year = LocalDate.now().getYear();

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

        return resolveHolidays(officialHolidays);
    }

    private static Set<LocalDate> resolveHolidays(Set<LocalDate> holidays) {

        Set<LocalDate> updatedHolidays = new TreeSet<>(holidays);

        holidays
                .stream()
                .filter(HolidayCalendarResolver::isWeekend)
                .forEach(date -> {
                    while (updatedHolidays.contains(date)) {
                        date = date.plusDays(1);
                    }
                    updatedHolidays.add(date);
                });

        return updatedHolidays;
    }

    private static boolean isWeekend(LocalDate day) {
        return day.getDayOfWeek().equals(SATURDAY) || day.getDayOfWeek().equals(SUNDAY);
    }

}

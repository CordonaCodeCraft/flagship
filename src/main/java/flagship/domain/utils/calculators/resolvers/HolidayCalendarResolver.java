package flagship.domain.utils.calculators.resolvers;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

public abstract class HolidayCalendarResolver {

    //todo: implement without nested while loop

    public static Set<LocalDate> resolve(final Set<LocalDate> holidays) {

        final Set<LocalDate> updatedHolidays = new TreeSet<>(holidays);

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

    private static boolean isWeekend(final LocalDate day) {
        return day.getDayOfWeek().equals(SATURDAY) || day.getDayOfWeek().equals(SUNDAY);
    }

}

package flagship.domain.calculators.servicedues;

import flagship.domain.calculators.resolvers.HolidayCalendarResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import static java.time.Month.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Holiday calendar resolver tests")
public class HolidayCalendarResolverTest {

    @DisplayName("Should return correct set of holiday dates")
    @Test
    void testReturnsCorrectSetOfHolidayDates() {

        LocalDate easter = LocalDate.of(2021, MAY, 2);

        Set<LocalDate> expected = new TreeSet<>();

        expected.add(easter);
        expected.add(LocalDate.of(2021, JANUARY, 1));
        expected.add(LocalDate.of(2021, MARCH, 3));
        expected.add(LocalDate.of(2021, APRIL, 30));
        expected.add(LocalDate.of(2021, MAY, 1));
        expected.add(LocalDate.of(2021, MAY, 3));
        expected.add(LocalDate.of(2021, MAY, 4));
        expected.add(LocalDate.of(2021, MAY, 6));
        expected.add(LocalDate.of(2021, MAY, 24));
        expected.add(LocalDate.of(2021, SEPTEMBER, 6));
        expected.add(LocalDate.of(2021, SEPTEMBER, 22));
        expected.add(LocalDate.of(2021, DECEMBER, 24));
        expected.add(LocalDate.of(2021, DECEMBER, 25));
        expected.add(LocalDate.of(2021, DECEMBER, 26));
        expected.add(LocalDate.of(2021, DECEMBER, 27));
        expected.add(LocalDate.of(2021, DECEMBER, 28));

        Set<LocalDate> officialHolidays = new TreeSet<>();

        officialHolidays.add(easter);
        officialHolidays.add(LocalDate.of(2021, JANUARY, 1));
        officialHolidays.add(LocalDate.of(2021, MARCH, 3));
        officialHolidays.add(LocalDate.of(2021, APRIL, 30));
        officialHolidays.add(LocalDate.of(2021, MAY, 1));
        officialHolidays.add(LocalDate.of(2021, MAY, 6));
        officialHolidays.add(LocalDate.of(2021, MAY, 24));
        officialHolidays.add(LocalDate.of(2021, SEPTEMBER, 6));
        officialHolidays.add(LocalDate.of(2021, SEPTEMBER, 22));
        officialHolidays.add(LocalDate.of(2021, DECEMBER, 24));
        officialHolidays.add(LocalDate.of(2021, DECEMBER, 25));
        officialHolidays.add(LocalDate.of(2021, DECEMBER, 26));

        Set<LocalDate> result = HolidayCalendarResolver.resolve(officialHolidays);

        assertThat(result.equals(expected)).isTrue();
    }

}


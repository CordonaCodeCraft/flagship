package flagship.domain.utils.calculators.servicedues;

import flagship.domain.utils.resolvers.HolidayCalendarResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import static java.time.Month.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// todo: Test will fail next year - change dates of the expected set

@DisplayName("Holiday calendar resolver tests")
public class HolidayCalendarResolverTest {

    @DisplayName("Should return correct set of holiday dates")
    @Test
    void testReturnsCorrectSetOfHolidayDates() {

        int year = LocalDate.now().getYear();

        LocalDate easter = LocalDate.of(year, MAY, 2);


        Set<LocalDate> expected = new TreeSet<>();

        expected.add(easter);

        expected.add(LocalDate.of(year, JANUARY, 1));
        expected.add(LocalDate.of(year, MARCH, 3));
        expected.add(LocalDate.of(year, APRIL, 30));
        expected.add(LocalDate.of(year, MAY, 1));
        expected.add(LocalDate.of(year, MAY, 3));
        expected.add(LocalDate.of(year, MAY, 4));
        expected.add(LocalDate.of(year, MAY, 6));
        expected.add(LocalDate.of(year, MAY, 24));
        expected.add(LocalDate.of(year, SEPTEMBER, 6));
        expected.add(LocalDate.of(year, SEPTEMBER, 22));
        expected.add(LocalDate.of(year, DECEMBER, 24));
        expected.add(LocalDate.of(year, DECEMBER, 25));
        expected.add(LocalDate.of(year, DECEMBER, 26));
        expected.add(LocalDate.of(year, DECEMBER, 27));
        expected.add(LocalDate.of(year, DECEMBER, 28));

        Set<LocalDate> result = HolidayCalendarResolver.resolveWithDateFor(easter);

        assertThat(result.equals(expected)).isTrue();

    }

}


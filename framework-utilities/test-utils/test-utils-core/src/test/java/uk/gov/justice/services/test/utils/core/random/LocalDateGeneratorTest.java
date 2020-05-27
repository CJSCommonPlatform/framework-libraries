package uk.gov.justice.services.test.utils.core.random;

import static java.time.LocalDate.now;
import static org.junit.rules.ExpectedException.none;
import static uk.gov.justice.services.test.utils.core.helper.TypeCheck.Times.times;
import static uk.gov.justice.services.test.utils.core.helper.TypeCheck.typeCheck;

import java.time.LocalDate;
import java.time.Period;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LocalDateGeneratorTest {

    private static final int NUMBER_OF_TIMES = 10000;

    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void shouldGenerateRandomFutureLocalDate() {
        final LocalDate startDate = now();
        final LocalDate endDate = startDate.plus(Period.ofYears(5));

        final Generator<LocalDate> futureLocalDateGenerator = new LocalDateGenerator(Period.ofYears(5),
                startDate, DateGenerator.Direction.FUTURE);

        typeCheck(futureLocalDateGenerator, s -> !(s.isBefore(startDate) || s.isAfter(endDate)))
                .verify(times(NUMBER_OF_TIMES));
    }

    @Test
    public void shouldGenerateRandomPastLocalDate() {
        final LocalDate startDate = now();
        final LocalDate endDate = startDate.minus(Period.ofYears(5));

        final Generator<LocalDate> pastLocalDateGenerator = new LocalDateGenerator(Period.ofYears(5),
                startDate, DateGenerator.Direction.PAST);

        typeCheck(pastLocalDateGenerator, s -> !(s.isBefore(endDate) || s.isAfter(startDate)))
                .verify(times(NUMBER_OF_TIMES));
    }
}

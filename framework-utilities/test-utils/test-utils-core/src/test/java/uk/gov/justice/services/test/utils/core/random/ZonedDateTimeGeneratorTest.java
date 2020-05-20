package uk.gov.justice.services.test.utils.core.random;

import static com.google.common.collect.Sets.newHashSet;
import static java.time.Period.ofYears;
import static java.time.ZoneOffset.UTC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.rules.ExpectedException.none;
import static uk.gov.justice.services.test.utils.core.helper.TypeCheck.Times.times;
import static uk.gov.justice.services.test.utils.core.helper.TypeCheck.typeCheck;
import static uk.gov.justice.services.test.utils.core.random.DateGenerator.Direction.PAST;

import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ZonedDateTimeGeneratorTest {

    private static final int NUMBER_OF_TIMES = 10000;

    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void shouldGenerateZonedDateTimeInUTCZone() {
        final ZonedDateTime utcZonedDateTime = new ZonedDateTimeGenerator(ofYears(1), ZonedDateTime.now(UTC), PAST, UTC).next();

        assertThat(utcZonedDateTime.getOffset().getId(), is("Z"));
        assertThat(utcZonedDateTime.getOffset().getTotalSeconds(), is(0));
    }

    @Test
    public void shouldGenerateDateTimesInDifferentZones() {
        final ZonedDateTimeGenerator randomZonedDateTimeGenerator = new ZonedDateTimeGenerator(ofYears(1), ZonedDateTime.now(UTC), PAST);
        final Set<ZoneId> randomZones = newHashSet();

        IntStream.range(0, NUMBER_OF_TIMES)
                .forEach(idx -> randomZones.add(randomZonedDateTimeGenerator.next().getZone()));

        assertThat(randomZones, hasSize(greaterThan(10)));
    }

    @Test
    public void shouldGenerateRandomFutureZonedDateTime() {
        final ZonedDateTime startDateTime = ZonedDateTime.now(UTC);
        final ZonedDateTime endDateTime = startDateTime.plus(Period.ofYears(5));

        final Generator<ZonedDateTime>  futureZonedDateTimeGenerator = new ZonedDateTimeGenerator(Period.ofYears(5),
                startDateTime, DateGenerator.Direction.FUTURE);

        typeCheck(futureZonedDateTimeGenerator, s -> !(s.isBefore(startDateTime) || s.isAfter(endDateTime)))
                .verify(times(NUMBER_OF_TIMES));
    }

    @Test
    public void shouldGenerateRandomPastZonedDateTime() {
        final ZonedDateTime startDateTime = ZonedDateTime.now(UTC);
        final ZonedDateTime endDateTime = startDateTime.minus(Period.ofYears(5));

        final Generator<ZonedDateTime>  pastZonedDateTimeGenerator = new ZonedDateTimeGenerator(Period.ofYears(5),
                startDateTime, DateGenerator.Direction.PAST);

        typeCheck(pastZonedDateTimeGenerator, s -> !(s.isBefore(endDateTime) || s.isAfter(startDateTime)))
                .verify(times(NUMBER_OF_TIMES));
    }
}

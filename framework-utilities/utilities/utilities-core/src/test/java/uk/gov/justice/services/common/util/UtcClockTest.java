package uk.gov.justice.services.common.util;

import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the {@link UtcClock} class.
 */
@ExtendWith(MockitoExtension.class)
public class UtcClockTest {

    @InjectMocks
    private UtcClock clock;

    @Test
    public void shouldGetANewZonedDateTimeWithCurrentTime() throws Exception {

        final ZonedDateTime zonedDateTime = clock.now();

        assertThat(zonedDateTime, is(notNullValue()));

        assertThat(zonedDateTime.isAfter(now().minusSeconds(2L)), is(true));
        assertThat(zonedDateTime.getZone(), is(UTC));
        assertThat(zonedDateTime.getNano() % 1_000_000, is(0));
    }
}

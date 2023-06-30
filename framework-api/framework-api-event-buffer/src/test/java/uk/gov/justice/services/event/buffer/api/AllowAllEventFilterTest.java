package uk.gov.justice.services.event.buffer.api;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AllowAllEventFilterTest {

    @Test
    public void shouldAllowAllEventNames() throws Exception {
        EventFilter filter = new AllowAllEventFilter();

        assertTrue(filter.accepts("event-nameA"));
        assertTrue(filter.accepts("aaa"));
        assertTrue(filter.accepts("bbb"));
    }
}

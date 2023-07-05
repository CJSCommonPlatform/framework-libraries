package uk.gov.justice.services.event.buffer.api;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AbstractEventFilterTest {

    private AbstractEventFilter filter;

    @Test
    public void shouldAllowConfiguredEventNames() throws Exception {
        filter = new AbstractEventFilter("event1", "event2", "event3") {};
        assertTrue(filter.accepts("event1"));
        assertTrue(filter.accepts("event2"));
        assertTrue(filter.accepts("event3"));
        assertFalse(filter.accepts("event4"));
    }
}

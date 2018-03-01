package uk.gov.justice.services.test.domain;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.services.test.DomainTest.eventNameFrom;
import static uk.gov.justice.services.test.DomainTest.generatedEventAsJsonNode;
import static uk.gov.justice.services.test.DomainTest.jsonNodeWithoutMetadataFrom;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

public class EventsChecker {

    public static void compareEvents(List<JsonNode> expectedEvents, List<Object> actualEvents) {

        assertThat(createMessage(expectedEvents, actualEvents, null), expectedEvents.size(), is(actualEvents.size()));

        int index = 0;
        for (JsonNode jsonNode : expectedEvents) {

            final Object generatedEvent = actualEvents.get(index);
            assertThat(createMessage(expectedEvents, actualEvents, eventNameFrom(generatedEvent)), eventNameFrom(generatedEvent), equalToIgnoringCase(eventNameFrom(jsonNode)));
            assertThat(createMessage(expectedEvents, actualEvents, null), generatedEventAsJsonNode(generatedEvent), equalTo(jsonNodeWithoutMetadataFrom(jsonNode)));
            index++;
        }
    }

    private static String createMessage(List<JsonNode> expectedEvents, List<Object> generatedEvents, String missingEvent) {
        final StringBuilder buffer = new StringBuilder();
        if (StringUtils.isNotEmpty(missingEvent)) {
            buffer.append("Event not found ").append(missingEvent).append(System.lineSeparator());
        }
        buffer.append("Expected Events [ ").append(expectedEvents.toString()).append(" ]").append(System.lineSeparator());
        buffer.append("Actual Events [ ");
        for (final Object generatedEvent : generatedEvents) {
            buffer.append(generatedEventAsJsonNode(generatedEvent).toString());
        }
        buffer.append("]");
        return buffer.toString();

    }
}

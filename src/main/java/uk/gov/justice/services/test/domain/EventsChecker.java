package uk.gov.justice.services.test.domain;

import static uk.gov.justice.services.test.DomainTest.eventNameFrom;
import static uk.gov.justice.services.test.DomainTest.generatedEventAsJsonNode;
import static uk.gov.justice.services.test.DomainTest.jsonNodeWithoutMetadataFrom;

import uk.gov.justice.services.test.exception.EventException;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

public class EventsChecker {
    public static void compareEvents(List<JsonNode> expectedEvents, List<Object> generatedEvents) {
        if (expectedEvents.size() > generatedEvents.size()) {
            throw new EventException(createMessage(expectedEvents, generatedEvents, null));
        }

        for (JsonNode jsonNode : expectedEvents) {

            boolean found = false;
            String expectedEventName = eventNameFrom(jsonNode);

            for (Object event : generatedEvents) {
                String generatedEventName = eventNameFrom(event);
                if(expectedEventName.equalsIgnoreCase(generatedEventName)) {
                    JsonNode generatedJsonNode = generatedEventAsJsonNode(event);
                    JsonNode expectedJsonNode = jsonNodeWithoutMetadataFrom(jsonNode);

                    if (!expectedJsonNode.equals(generatedJsonNode)) {
                        throw new EventException(createMessage(expectedEvents, generatedEvents, null));
                    }
                    found = true;
                }
            }

            if (!found) {
                throw new EventException(createMessage(expectedEvents, generatedEvents, expectedEventName));
            }
        }
    }

    private static String createMessage(List<JsonNode> expectedEvents, List<Object> generatedEvents, String missingEvent) {
        final StringBuilder buffer = new StringBuilder();
        if (StringUtils.isNotEmpty(missingEvent)) {
            buffer.append("Event not found ").append(missingEvent).append(System.lineSeparator());
        }
        buffer.append("Expected Events [ ").append(expectedEvents.toString()).append(" ]").append(System.lineSeparator());
        buffer.append("Generated Events [ ");
        for (final Object generatedEvent : generatedEvents) {
            buffer.append(generatedEventAsJsonNode(generatedEvent).toString());
        }
        buffer.append("]");
        return buffer.toString();

    }
}

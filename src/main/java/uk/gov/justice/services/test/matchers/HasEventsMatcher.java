package uk.gov.justice.services.test.matchers;

import static java.lang.System.lineSeparator;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class HasEventsMatcher extends TypeSafeDiagnosingMatcher<List<JsonNode>> {

    private final List<JsonNode> expectedEvents;

    public HasEventsMatcher(final List<JsonNode> expectedEvents) {
        this.expectedEvents = expectedEvents;
    }

    @Override
    protected boolean matchesSafely(final List<JsonNode> actualEvents, final Description description) {

        final int expectedEventsSize = expectedEvents.size();
        final int actualEventsSize = actualEvents.size();

        if (actualEventsSize >= expectedEventsSize) {
            if (startsWith(expectedEvents, actualEvents)) {
                return true;
            }
        }

        if (actualEventsSize == 0) {
            description.appendText("no events found in stream");
        } else {
            if (actualEventsSize == 1) {
                description.appendText("found this event in stream:" + lineSeparator() + actualEvents.get(0));
            } else {
                description.appendText("found these events in stream:" + lineSeparator() + actualEvents);
            }
        }

        return false;
    }

    @Override
    public void describeTo(final Description description) {

        final int expectedEventsSize = expectedEvents.size();

        if (expectedEventsSize == 0) {
            description.appendText("no events");
        } else {
            if (expectedEventsSize == 1) {
                description.appendText("this event in stream:" + lineSeparator() + expectedEvents.get(0));
            } else {
                description.appendText("these " + expectedEventsSize + " events in stream:" + lineSeparator() + expectedEvents);
            }
        }
    }

    private boolean startsWith(final List<JsonNode> expectedEvents, final List<JsonNode> actualEvents) {

        for (int i = 0; i < expectedEvents.size(); i++) {
            if (!actualEvents.get(i).equals(expectedEvents.get(i))) {
                return false;
            }
        }

        return true;
    }

    public static Matcher<List<JsonNode>> hasEvents(final List<JsonNode> expectedEvents) {
        return new HasEventsMatcher(expectedEvents);
    }
}

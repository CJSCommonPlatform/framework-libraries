package uk.gov.justice.services.test.matchers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class NoEventsMatcher extends TypeSafeDiagnosingMatcher<List<JsonNode>> {

    
    @Override
    protected boolean matchesSafely(final List<JsonNode> actualEvents, final Description description) {

        final int actualEventsSize = actualEvents.size();

        if (actualEventsSize == 0) {
            return true;
        }

        if (actualEventsSize == 1) {
            description.appendText("found 1 event:\n" + actualEvents.get(0));
        } else {
            description.appendText("found " + actualEventsSize + " events:\n" + actualEvents);
        }

        return false;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("no events");
    }

    public static Matcher<List<JsonNode>> hasNoEvents() {
        return new NoEventsMatcher();
    }
}

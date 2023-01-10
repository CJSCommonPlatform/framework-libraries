package uk.gov.justice.services.test.matchers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NoEventsMatcherTest {

    @InjectMocks
    private NoEventsMatcher noEventsMatcher;

    @Test
    public void shouldReturnTrueIfActualEventsListIsEmpty() throws Exception {

        final List<JsonNode> actualEvents = emptyList();

        final Description description = mock(Description.class);

        assertThat(noEventsMatcher.matchesSafely(actualEvents, description), is(true));

        verifyNoInteractions(description);
    }

    @Test
    public void shouldReturnFalseIfActualEventsListHasOneElement() throws Exception {

        final JsonNode event_1 = mock(JsonNode.class, "event_1");
        final Description description = mock(Description.class);

        final List<JsonNode> actualEvents = singletonList(event_1);

        assertThat(noEventsMatcher.matchesSafely(actualEvents, description), is(false));

        verify(description).appendText("found 1 event:\n" + event_1);
    }

    @Test
    public void shouldReturnFalseIfActualEventsListHasMoreThanOneElement() throws Exception {

        final JsonNode event_1 = mock(JsonNode.class, "event_1");
        final JsonNode event_2 = mock(JsonNode.class, "event_2");
        final Description description = mock(Description.class);

        final List<JsonNode> actualEvents = asList(event_1, event_2);

        assertThat(noEventsMatcher.matchesSafely(actualEvents, description), is(false));

        verify(description).appendText("found 2 events:\n" + actualEvents);
    }

    @Test
    public void shouldDescribePreferredBehaviour() throws Exception {

        final Description description = mock(Description.class);

        noEventsMatcher.describeTo(description);

        verify(description).appendText("no events");
    }
}

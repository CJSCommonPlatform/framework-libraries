package uk.gov.justice.services.test.matchers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HasEventsMatcherTest {

    @Test
    public void shouldReturnTrueIfExpectedEventsMatchesExactlyTheActualEventList() throws Exception {

        final JsonNode event_1 = create("event_1");
        final JsonNode event_2 = create("event_2");
        final JsonNode event_3 = create("event_3");

        final Description description = mock(Description.class);

        final List<JsonNode> expectedEvents = asList(event_1, event_2, event_3);
        final List<JsonNode> actualEvents = asList(event_1, event_2, event_3);

        final HasEventsMatcher hasEventsMatcher = new HasEventsMatcher(expectedEvents);

        assertThat(hasEventsMatcher.matchesSafely(actualEvents, description), is(true));
    }

    @Test
    public void shouldReturnTrueIfAllExpectedEventsAreInTheActualEventList() throws Exception {

        final JsonNode event_1 = create("event_1");
        final JsonNode event_2 = create("event_2");
        final JsonNode event_3 = create("event_3");

        final Description description = mock(Description.class);

        final List<JsonNode> expectedEvents = asList(event_1, event_2);
        final List<JsonNode> actualEvents = asList(event_1, event_2, event_3);

        final HasEventsMatcher hasEventsMatcher = new HasEventsMatcher(expectedEvents);

        assertThat(hasEventsMatcher.matchesSafely(actualEvents, description), is(true));
    }

    @Test
    public void shouldReturnTrueIfZeroEventsAreInTheExpectedAndActualActualEventList() throws Exception {


        final Description description = mock(Description.class);

        final List<JsonNode> expectedEvents = emptyList();
        final List<JsonNode> actualEvents = emptyList();

        final HasEventsMatcher hasEventsMatcher = new HasEventsMatcher(expectedEvents);

        assertThat(hasEventsMatcher.matchesSafely(actualEvents, description), is(true));
    }

    @Test
    public void shouldReturnFalseIfNotAllExpectedEventsAreInTheActualEventList() throws Exception {
        final JsonNode event_1 = create("event_1");
        final JsonNode event_2 = create("event_2");
        final JsonNode event_3 = create("event_3");

        final Description description = mock(Description.class);

        final List<JsonNode> expectedEvents = asList(event_1, event_2, event_3);
        final List<JsonNode> actualEvents = asList(event_1, event_2);

        final HasEventsMatcher hasEventsMatcher = new HasEventsMatcher(expectedEvents);

        assertThat(hasEventsMatcher.matchesSafely(actualEvents, description), is(false));
    }

    @Test
    public void shouldReturnFalseIfExpectedEventsAreInTheActualEventListInTheWrongOrder() throws Exception {

        final JsonNode event_1 = create("event_1");
        final JsonNode event_2 = create("event_2");
        final JsonNode event_3 = create("event_3");

        final Description description = mock(Description.class);

        final List<JsonNode> expectedEvents = asList(event_1, event_2, event_3);
        final List<JsonNode> actualEvents = asList(event_3, event_1, event_2);

        final HasEventsMatcher hasEventsMatcher = new HasEventsMatcher(expectedEvents);

        assertThat(hasEventsMatcher.matchesSafely(actualEvents, description), is(false));
    }

    @Test
    public void shouldReturnFalseAndGiveCorrectMessageForNoActualEvents() throws Exception {

        final JsonNode event_1 = create("event_1");
        final JsonNode event_2 = create("event_2");
        final JsonNode event_3 = create("event_3");

        final Description description = mock(Description.class);

        final List<JsonNode> expectedEvents = asList(event_1, event_2, event_3);
        final List<JsonNode> actualEvents = emptyList();

        final HasEventsMatcher hasEventsMatcher = new HasEventsMatcher(expectedEvents);

        assertThat(hasEventsMatcher.matchesSafely(actualEvents, description), is(false));

        verify(description).appendText("no events found in stream");
    }

    @Test
    public void shouldReturnFalseAndGiveCorrectMessageForOneActualEvent() throws Exception {

        final JsonNode event_1 = create("event_1");
        final JsonNode event_2 = create("event_2");
        final JsonNode event_3 = create("event_3");

        final Description description = mock(Description.class);

        final List<JsonNode> expectedEvents = asList(event_1, event_2, event_3);
        final List<JsonNode> actualEvents = singletonList(event_1);

        final HasEventsMatcher hasEventsMatcher = new HasEventsMatcher(expectedEvents);

        assertThat(hasEventsMatcher.matchesSafely(actualEvents, description), is(false));

        verify(description).appendText("found this event in stream:\n" + event_1);
    }

    @Test
    public void shouldReturnFalseAndGiveCorrectMessageForMoreThanOneActualEvents() throws Exception {

        final JsonNode event_1 = create("event_1");
        final JsonNode event_2 = create("event_2");
        final JsonNode event_3 = create("event_3");

        final Description description = mock(Description.class);

        final List<JsonNode> expectedEvents = asList(event_1, event_2, event_3);
        final List<JsonNode> actualEvents = asList(event_1, event_2);

        final HasEventsMatcher hasEventsMatcher = new HasEventsMatcher(expectedEvents);

        assertThat(hasEventsMatcher.matchesSafely(actualEvents, description), is(false));

        verify(description).appendText("found these events in stream:\n" + actualEvents);
    }

    @Test
    public void shouldHaveCorrectDescriptionForZeroExpectedEvents() throws Exception {

        final List<JsonNode> expectedEvents = emptyList();

        final Description description = mock(Description.class);

        final HasEventsMatcher hasEventsMatcher = new HasEventsMatcher(expectedEvents);

        hasEventsMatcher.describeTo(description);

        verify(description).appendText("no events");
    }

    @Test
    public void shouldHaveCorrectDescriptionForOneExpectedEvent() throws Exception {

        final JsonNode event_1 = create("event_1");

        final List<JsonNode> expectedEvents = singletonList(event_1);

        final Description description = mock(Description.class);

        final HasEventsMatcher hasEventsMatcher = new HasEventsMatcher(expectedEvents);

        hasEventsMatcher.describeTo(description);

        verify(description).appendText("this event in stream:\n" + event_1);
    }

    @Test
    public void shouldHaveCorrectDescriptionForMoreThanOneExpectedEvent() throws Exception {

        final JsonNode event_1 = create("event_1");
        final JsonNode event_2 = create("event_2");

        final List<JsonNode> expectedEvents = asList(event_1, event_2);

        final Description description = mock(Description.class);

        final HasEventsMatcher hasEventsMatcher = new HasEventsMatcher(expectedEvents);

        hasEventsMatcher.describeTo(description);

        verify(description).appendText("these 2 events in stream:\n" + expectedEvents);
    }

    private JsonNode create(final String name) {
        final JsonNode jsonNode = mock(JsonNode.class, name);
        when(jsonNode.fieldNames()).thenReturn(mock(Iterator.class));
        return jsonNode;
    }
}

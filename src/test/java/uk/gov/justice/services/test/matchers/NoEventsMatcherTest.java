package uk.gov.justice.services.test.matchers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Description;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class NoEventsMatcherTest {

    @InjectMocks
    private NoEventsMatcher noEventsMatcher;

    @Test
    public void shouldReturnTrueIfActualEventsListIsEmpty() throws Exception {

        final List<JsonNode> actualEvents = emptyList();

        final Description description = mock(Description.class);

        assertThat(noEventsMatcher.matchesSafely(actualEvents, description), is(true));

        verifyZeroInteractions(description);
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

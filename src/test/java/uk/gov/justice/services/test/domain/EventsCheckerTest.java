package uk.gov.justice.services.test.domain;

import static uk.gov.justice.services.test.DomainTest.eventsFromFileNames;
import static uk.gov.justice.services.test.DomainTest.jsonNodesFrom;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class EventsCheckerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void expectedEventsListSizeShouldbeEqualToActualEventsPresent() throws Exception {
        List<JsonNode> expectedEvents = jsonNodesFrom("something happened");
        List<Object> actualEvents = eventsFromFileNames("something happened");

        EventsChecker.compareEvents(expectedEvents, actualEvents);
    }

    @Test
    public void shouldFailWhenActualAndExpectedEventsAreNotEqual() throws Exception {
        List<JsonNode> expectedEvents = jsonNodesFrom("something happened");
        List<Object> actualEvents = new ArrayList<>();

        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("Expected Events [ [{\"_metadata\":{\"name\":\"something-happened\"},\"id\":\"5c5a1d30-0414-11e7-93ae-92361f002671\"}] ]\n" +
                "Actual Events [ ]");

        EventsChecker.compareEvents(expectedEvents, actualEvents);
    }

    @Test
    public void expectedEventShouldbePresentInActualEventsList() throws Exception {
        List<JsonNode> expectedEvents = jsonNodesFrom("something happened");
        List<Object> actualEvents = eventsFromFileNames("something-else-happened");

        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("Event not found something-else-happened\n" +
                "Expected Events [ [{\"_metadata\":{\"name\":\"something-happened\"},\"id\":\"5c5a1d30-0414-11e7-93ae-92361f002671\"}] ]\n" +
                "Actual Events [ {\"id\":\"5c5a1d30-0414-11e7-93ae-92361f002671\"}]");

        EventsChecker.compareEvents(expectedEvents, actualEvents);
    }
}
package uk.gov.justice.services.test.matchers;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HasEventsWithWildcardsMatcherTest {

    private static final String EVENT_PAYLOAD = " { \"someNode\": { \"field1\": \"value1\" } } ";

    private static final String EVENT_PAYLOAD_WITH_WILDCARD = "{\"someNode\":{\"field1\":\"*\"}}";

    private final ObjectMapper mapper = new ObjectMapperProducer().objectMapper();

    @Test
    public void shouldPassWildcardMatchingWhenInExpected() throws Exception {
        final JsonNode sourceEvent = mapper.readTree(EVENT_PAYLOAD);

        final List<JsonNode> actualEvents = asList(sourceEvent);
        final List<JsonNode> expectedEvents = asList(mapper.readTree(EVENT_PAYLOAD_WITH_WILDCARD));

        final Description description = mock(Description.class);

        final HasEventsMatcher hasEventsMatcher = new HasEventsMatcher(expectedEvents);

        assertThat(hasEventsMatcher.matchesSafely(actualEvents, description), is(true));
    }

    @Test
    public void shouldFailWildcardMatchingWhenInActualButNotExpected() throws Exception {
        final JsonNode sourceEvent = mapper.readTree(EVENT_PAYLOAD_WITH_WILDCARD);

        final List<JsonNode> actualEvents = asList(sourceEvent);
        final List<JsonNode> expectedEvents = asList(mapper.readTree(EVENT_PAYLOAD));

        final Description description = mock(Description.class);

        final HasEventsMatcher hasEventsMatcher = new HasEventsMatcher(expectedEvents);

        assertThat(hasEventsMatcher.matchesSafely(actualEvents, description), is(false));
    }
}

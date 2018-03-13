package uk.gov.justice.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.domain.EventBuilder.event;

import uk.gov.justice.domain.subscriptiondescriptor.Event;

import org.junit.Test;

public class EventBuilderTest {

    @Test
    public void shouldBuildAnEvent() throws Exception {

        final String name = "event name";
        final String schemaUri = "schemaUri";

        final Event event = event()
                .withName(name)
                .withSchemaUri(schemaUri)
                .build();

        assertThat(event.getName(), is(name));
        assertThat(event.getSchemaUri(), is(schemaUri));
    }
}

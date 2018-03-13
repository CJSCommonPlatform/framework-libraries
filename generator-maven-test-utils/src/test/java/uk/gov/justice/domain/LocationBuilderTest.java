package uk.gov.justice.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.domain.LocationBuilder.location;

import uk.gov.justice.domain.subscriptiondescriptor.Location;

import org.junit.Test;

public class LocationBuilderTest {

    @Test
    public void shouldBuildALocation() throws Exception {

        final String jmsUri = "jmsUri";
        final String restUri = "restUri";

        final Location location = location()
                .withJmsUri(jmsUri)
                .withRestUri(restUri)
                .build();

        assertThat(location.getJmsUri(), is(jmsUri));
        assertThat(location.getRestUri(), is(restUri));
    }
}

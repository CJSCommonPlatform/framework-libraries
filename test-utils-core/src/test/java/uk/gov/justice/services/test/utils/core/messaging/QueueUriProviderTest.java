package uk.gov.justice.services.test.utils.core.messaging;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.services.test.utils.common.host.TestHostProvider.INTEGRATION_HOST_KEY;
import static uk.gov.justice.services.test.utils.common.host.TestHostProvider.ARTEMIS_HOST_KEY;
import static uk.gov.justice.services.test.utils.core.messaging.QueueUriProvider.queueUri;
import static uk.gov.justice.services.test.utils.core.messaging.QueueUriProvider.artemisQueueUri;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class QueueUriProviderTest {

    private static final String ARTEMIS_URI = "ARTEMIS_URI";

    @InjectMocks
    private QueueUriProvider queueUriProvider;

    @Before
    @After
    public void clearSystemProperty() {
        System.clearProperty(INTEGRATION_HOST_KEY);
        System.clearProperty(ARTEMIS_HOST_KEY);
        System.clearProperty(ARTEMIS_URI);
    }

    @Test
    public void shouldGetLocalhostUriByDefault() {

        final String localhostUri = "tcp://localhost:61616";

        assertThat(queueUriProvider.getQueueUri(), is(localhostUri));
        assertThat(queueUri(), is(localhostUri));
    }

    @Test
    public void shouldGetTheRemoteUriIfTheSystemPropertyIsSet() {

        System.setProperty(INTEGRATION_HOST_KEY, "my.host.com");

        final String remoteUri = "tcp://my.host.com:61616";

        assertThat(queueUriProvider.getQueueUri(), is(remoteUri));
        assertThat(queueUri(), is(remoteUri));
    }

    @Test
    public void shouldGetLocalhostUriByDefaultForArtemis() {

        final String localhostUri = "tcp://localhost:61616";

        assertThat(queueUriProvider.getArtemisQueueUri(), is(localhostUri));
        assertThat(artemisQueueUri(), is(localhostUri));
    }

    @Test
    public void shouldGetArtemisUriFromSystemProperty() {

        final String userDefinedUri = "tcp://myServer:61616?debug=true";
        System.setProperty(ARTEMIS_URI, userDefinedUri);

        assertThat(queueUriProvider.getArtemisQueueUri(), is(userDefinedUri));
        assertThat(artemisQueueUri(), is(userDefinedUri));
    }

    @Test
    public void shouldGetTheRemoteUriForArtemisIfTheSystemPropertyIsSet() {

        System.setProperty(ARTEMIS_HOST_KEY, "my.host.com");

        final String remoteUri = "tcp://my.host.com:61616";

        assertThat(queueUriProvider.getArtemisQueueUri(), is(remoteUri));
        assertThat(artemisQueueUri(), is(remoteUri));
    }
}

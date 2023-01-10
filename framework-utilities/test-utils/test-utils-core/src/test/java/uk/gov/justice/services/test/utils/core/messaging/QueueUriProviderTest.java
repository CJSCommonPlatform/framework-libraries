package uk.gov.justice.services.test.utils.core.messaging;

import static com.google.common.collect.Lists.newArrayList;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.common.host.TestHostProvider.ARTEMIS_HOST_KEY;
import static uk.gov.justice.services.test.utils.common.host.TestHostProvider.INTEGRATION_HOST_KEY;
import static uk.gov.justice.services.test.utils.core.messaging.QueueUriProvider.artemisQueueUri;
import static uk.gov.justice.services.test.utils.core.messaging.QueueUriProvider.queueUri;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;


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
    public void shouldGetLocalhostUriBySuppliedPort() {

        final int port = 61646;
        final String localhostUri = "tcp://localhost:" + port;

        assertThat(queueUriProvider.getQueueUri(61646), is(localhostUri));
        assertThat(queueUri(port), is(localhostUri));
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

        assertThat(queueUriProvider.getArtemisQueueUri(), is(asList(localhostUri)));
        assertThat(artemisQueueUri(), is(asList(localhostUri)));
    }

    @Test
    public void shouldGetArtemisUriFromSystemProperty() {

        final String userDefinedUri = "tcp://myServer:61616?debug=true";
        System.setProperty(ARTEMIS_URI, userDefinedUri);

        assertThat(queueUriProvider.getArtemisQueueUri(), is(asList(userDefinedUri)));
        assertThat(artemisQueueUri(), is(asList(userDefinedUri)));
    }

    @Test
    public void shouldGetArtemisUriFromSystemPropertyAfterSplittingTheEntries() {

        final String userDefinedUri1 = "tcp://myServer2:61616?debug=true";
        final String userDefinedUri2 = "tcp://myServer2:61616?debug=true";
        final String userDefinedUri = userDefinedUri1 + ","+ userDefinedUri2;
        System.setProperty(ARTEMIS_URI, userDefinedUri);

        assertThat(queueUriProvider.getArtemisQueueUri(), is(newArrayList(userDefinedUri1, userDefinedUri2)));
        assertThat(artemisQueueUri(), is(newArrayList(userDefinedUri1, userDefinedUri2)));
    }

    @Test
    public void shouldGetTheRemoteUriForArtemisIfTheSystemPropertyIsSet() {

        System.setProperty(ARTEMIS_HOST_KEY, "my.host.com");

        final String remoteUri = "tcp://my.host.com:61616";

        assertThat(queueUriProvider.getArtemisQueueUri(), is(asList(remoteUri)));
        assertThat(artemisQueueUri(), is(asList(remoteUri)));
    }
}

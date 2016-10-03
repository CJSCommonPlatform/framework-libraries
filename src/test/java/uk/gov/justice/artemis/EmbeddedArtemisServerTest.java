package uk.gov.justice.artemis;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.apache.activemq.artemis.spi.core.security.ActiveMQSecurityManager;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedArtemisServerTest {

    EmbeddedJMS embeddedJMS;

    @Before
    public void before() {
        embeddedJMS = mock(EmbeddedJMS.class);
        EmbeddedArtemisServer.setJmsServer(embeddedJMS);
    }

    @Test
    public void testStartServer() throws Exception {

        EmbeddedArtemisServer.startServer();

        verify(embeddedJMS, times(1)).setConfigResourcePath("broker.xml");

        verify(embeddedJMS, times(1)).setSecurityManager(any(ActiveMQSecurityManager.class));

        verify(embeddedJMS, times(1)).start();
    }

    @Test(expected=java.net.MalformedURLException.class)
    public void testStartServerWithoutSetup() throws Exception {

        EmbeddedArtemisServer.setJmsServer(null);

        EmbeddedArtemisServer.startServer();

        verify(embeddedJMS, times(1)).setConfigResourcePath("broker.xml");
    }

    @Test
    public void testStopServer() throws Exception {
       
        EmbeddedArtemisServer.stopServer();
        
        verify(embeddedJMS, times(1)).stop();
    }

}

package uk.gov.justice.artemis;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.apache.activemq.artemis.spi.core.security.ActiveMQSecurityManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedArtemisServerTest {

    static EmbeddedJMS embeddedJMS;

    @Before
    public void before() {
        embeddedJMS = mock(EmbeddedJMS.class);
        EmbeddedArtemisServer.setJmsServer(embeddedJMS);
    }

    @After
    public void after()throws Exception {
        EmbeddedArtemisServer.stopServer();        
    }

    @Test
    public void shouldStartTheServerInvokingAllTheMethods() throws Exception {
        try {
            EmbeddedArtemisServer.startServer();

            verify(embeddedJMS, times(1)).setConfigResourcePath("broker.xml");

            verify(embeddedJMS, times(1)).setSecurityManager(any(ActiveMQSecurityManager.class));

            verify(embeddedJMS, times(1)).start();

        } catch (Exception e) {

            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void shouldBeAbleToStartAndStopTheServerMultipleTimes() throws Exception {
        try {

            EmbeddedArtemisServer.startServer();

            verify(embeddedJMS, times(1)).start();

            EmbeddedArtemisServer.stopServer();

            verify(embeddedJMS, times(1)).stop();

            EmbeddedArtemisServer.startServer();

            verify(embeddedJMS, times(2)).start();

        } catch (Exception e) {

            Assert.fail(e.getMessage());

        }

    }

    @Test
    public void shouldStartTheServerOnlyOnce() {
        try {

            EmbeddedArtemisServer.startServer();

            EmbeddedArtemisServer.startServer();

            verify(embeddedJMS, times(1)).start();

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    
    @Test
    public void shouldStopTheServerOnlyOnce() {
        try {

            EmbeddedArtemisServer.startServer();

            EmbeddedArtemisServer.stopServer();
            
            EmbeddedArtemisServer.stopServer();

            verify(embeddedJMS, times(1)).stop();

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

}

package uk.gov.justice.artemis;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.net.MalformedURLException;

import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.apache.activemq.artemis.spi.core.security.ActiveMQSecurityManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EmbeddedArtemisServerTest {

    @Mock
    EmbeddedJMS embeddedJMS;

    @Before
    public void before() {
        EmbeddedArtemisServer.setJmsServer(embeddedJMS);
    }

    @After
    public void after() throws Exception {
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

    @Test(expected=MalformedURLException.class)
    public void shouldInitialiseTheServerWithDefaultInstance() throws Exception {
        
        EmbeddedArtemisServer.setJmsServer(null);

        EmbeddedArtemisServer.startServer();

        verify(embeddedJMS, times(1)).setConfigResourcePath("broker.xml");
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

    @Test
    public void shouldTestSetter() {
        EmbeddedArtemisServer.setJmsServer(embeddedJMS);
        assertEquals(embeddedJMS, EmbeddedArtemisServer.getJmsServer());
    }

    @Test(expected = IllegalAccessException.class)
    public void shouldTestPrivateConstructor()
                    throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        Class.forName(EmbeddedArtemisServer.class.getCanonicalName()).newInstance();
    }

}

package uk.gov.justice.artemis;


import static net.trajano.commons.testing.UtilityClassTestUtil.assertUtilityClassWellDefined;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.Semaphore;

import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.junit.Test;

public class EmbeddedArtemisServerTest {

    @Test
    public void shouldBeWellDefinedUtilityClass() {
        assertUtilityClassWellDefined(EmbeddedArtemisServer.class);
    }

    @Test
    public void shouldStartServerUsingMainMethod() throws Exception {

        EmbeddedJMSServer es = mock(EmbeddedJMSServer.class);
        EmbeddedArtemisServer.setJmsServer(es);
        EmbeddedArtemisServer.main(null);

        verify(es, times(1)).start();
    }

    @Test
    public void shouldStartServer() throws Exception {

        EmbeddedJMSServer es = mock(EmbeddedJMSServer.class);
        EmbeddedArtemisServer.setJmsServer(es);
        EmbeddedArtemisServer.startServer();

        verify(es, times(1)).start();
    }

    @Test
    public void shouldNotStartServerIfServerPermitFails() throws Exception {

        EmbeddedJMSServer es = mock(EmbeddedJMSServer.class);
        ServerPermit sp = mock(ServerPermit.class);

        EmbeddedArtemisServer.setJmsServer(es);
        EmbeddedArtemisServer.setServerPermit(sp);

        doThrow(new InterruptedException()).when(sp).acquire();

        try {
            EmbeddedArtemisServer.startServer();
        } catch (InterruptedException ie) {
        }
        verify(es, never()).start();
    }


    @Test
    public void shouldReleaseServerPermitIfStartServerFails() throws Exception {

        EmbeddedJMSServer es = mock(EmbeddedJMSServer.class);
        Semaphore sem = mock(Semaphore.class);
        ServerPermit sp = new ServerPermit();
        sp.setSemaphore(sem);

        EmbeddedArtemisServer.setJmsServer(es);
        EmbeddedArtemisServer.setServerPermit(sp);

        doThrow(new Exception()).when(es).start();

        try {
            EmbeddedArtemisServer.startServer();
        } catch (Exception e) {
        }
        verify(sem, times(1)).release();
    }


    @Test
    public void shouldStopServer() throws Exception {
        EmbeddedJMSServer es = mock(EmbeddedJMSServer.class);
        EmbeddedArtemisServer.setJmsServer(es);
        EmbeddedArtemisServer.stopServer();

        verify(es, times(1)).stop();
    }

    @Test
    public void shouldNotStopServerIfServerPermitFails() throws Exception {

        EmbeddedJMSServer es = mock(EmbeddedJMSServer.class);
        ServerPermit sp = mock(ServerPermit.class);

        EmbeddedArtemisServer.setJmsServer(es);
        EmbeddedArtemisServer.setServerPermit(sp);

        doThrow(new InterruptedException()).when(sp).acquire();

        EmbeddedArtemisServer.stopServer();
        verify(es, never()).stop();
    }


    @Test
    public void shouldReleaseServerPermitIfStopServerFails() throws Exception {

        EmbeddedJMSServer es = mock(EmbeddedJMSServer.class);
        Semaphore sem = mock(Semaphore.class);
        ServerPermit sp = new ServerPermit();
        sp.setSemaphore(sem);

        EmbeddedArtemisServer.setJmsServer(es);
        EmbeddedArtemisServer.setServerPermit(sp);

        doThrow(new Exception()).when(es).stop();

        try {
            EmbeddedArtemisServer.stopServer();
        } catch (Exception e) {
        }
        
        verify(sem, times(1)).release();
    }

    @Test
    public void shouldSetAndGetJmsServer() {
        EmbeddedJMSServer es = mock(EmbeddedJMSServer.class);
        EmbeddedArtemisServer.setJmsServer(es);

        assertEquals(es, EmbeddedArtemisServer.getJmsServer());
    }

    @Test
    public void shouldSetAndGetServerPermit() {
        ServerPermit sp = mock(ServerPermit.class);
        EmbeddedArtemisServer.setServerPermit(sp);

        assertEquals(sp, EmbeddedArtemisServer.getServerPermit());
    }


    @Test
    public void shouldTestShutdownHookHandlesException() throws Exception {
        EmbeddedJMSServer es = mock(EmbeddedJMSServer.class);
        EmbeddedArtemisServer.setJmsServer(es);

        doThrow(new Exception("Shutdown Exception")).when(es).stop();
        Thread t = EmbeddedArtemisServer.getShutDownHook();
        t.run();
    }

    @Test
    public void shouldHandleThrownExceptionWhenInvokingStop() throws Exception {

        EmbeddedJMSServer es = mock(EmbeddedJMSServer.class);
        EmbeddedArtemisServer.setJmsServer(es);

        doThrow(new Exception()).when(es).stop();

        assertEquals(false, EmbeddedArtemisServer.stopServer());
    }
    
    @Test
    public void shouldSetEmbeddedJMS() {
        EmbeddedJMS jms = mock(EmbeddedJMS.class);
        EmbeddedJMSServer es = mock(EmbeddedJMSServer.class);
        
        EmbeddedArtemisServer.setJmsServer(es);
        EmbeddedArtemisServer.setEmbeddedJms(jms);

        verify(es).setJmsServer(jms);
    }

    @Test
    public void shouldGetEmbeddedJMS() {
        EmbeddedJMS jms = mock(EmbeddedJMS.class);
        EmbeddedJMSServer es = new EmbeddedJMSServer();
        
        EmbeddedArtemisServer.setJmsServer(es);
        EmbeddedArtemisServer.setEmbeddedJms(jms);

        assertEquals(jms, EmbeddedArtemisServer.getEmbeddedJms());
    }

}


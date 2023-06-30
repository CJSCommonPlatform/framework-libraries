package uk.gov.justice.artemis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmbeddedJMSServerTest {

    EmbeddedJMSServer embeddedJMSServer;
    
    @BeforeEach
    public void before() {
        embeddedJMSServer = new EmbeddedJMSServer();
    }
    
    @Test
    public void shouldStartJMSServerIfInitialisedIsFalse() throws Exception {
        
        EmbeddedJMS jms = mock(EmbeddedJMS.class);
        embeddedJMSServer.setInitialised(false);
        embeddedJMSServer.setJmsServer(jms);
        
        embeddedJMSServer.start();
        
        assertTrue(embeddedJMSServer.isInitialised());
        
        verify(jms, times(1)).start();        
    }
    
    @Test
    public void shouldNotStartJMSServerIfInitialisedIsTrue() throws Exception {
        
        EmbeddedJMS jms = mock(EmbeddedJMS.class);
        embeddedJMSServer.setInitialised(true);
        embeddedJMSServer.setJmsServer(jms);
        
        embeddedJMSServer.start();
        
        assertTrue(embeddedJMSServer.isInitialised());
        
        verify(jms, never()).start();        
    }

    @Test
    public void shouldNotStopJMSServerIfInitialisedIsFalse() throws Exception {
        EmbeddedJMS jms = mock(EmbeddedJMS.class);
        embeddedJMSServer.setInitialised(false);
        embeddedJMSServer.setJmsServer(jms);
      
        embeddedJMSServer.stop();
        
        assertFalse(embeddedJMSServer.isInitialised());
        
        verify(jms, never()).stop();
    }

    @Test
    public void shouldStopJMSServerIfInitialisedIsTrue() throws Exception {
        EmbeddedJMS jms = mock(EmbeddedJMS.class);
        embeddedJMSServer.setInitialised(true);
        embeddedJMSServer.setJmsServer(jms);
      
        embeddedJMSServer.stop();
        
        assertFalse(embeddedJMSServer.isInitialised());
        
        verify(jms, times(1)).stop();
    }
    
    @Test
    public void shouldGetJmsServer() {
        EmbeddedJMS jms = mock(EmbeddedJMS.class);
        embeddedJMSServer.setJmsServer(jms);
        assertEquals(jms, embeddedJMSServer.getJmsServer());
    }

    @Test
    public void shouldSetInitialised() {
        embeddedJMSServer.setInitialised(true);
        assertTrue(embeddedJMSServer.isInitialised());
    }

}

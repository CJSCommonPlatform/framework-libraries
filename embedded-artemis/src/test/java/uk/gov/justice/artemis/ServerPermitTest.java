package uk.gov.justice.artemis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.concurrent.Semaphore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServerPermitTest {

    ServerPermit serverPermit;

    @BeforeEach
    public void before() {
        serverPermit = new ServerPermit();
    }

    @Test
    public void shouldCreateFairServerPermit() {
        assertNotNull(serverPermit.getSemaphore());
        assertEquals(1, serverPermit.getSemaphore().availablePermits());
        assertTrue(serverPermit.getSemaphore().isFair());
    }

    @Test
    public void shouldAcquireSemaphorePermit() throws InterruptedException {
        Semaphore semaphore = mock(Semaphore.class);
        serverPermit.setSemaphore(semaphore);
        serverPermit.acquire();
        verify(semaphore).acquire();
    }

    @Test
    public void shouldReleaseSemaphorePermit() throws Exception {
        Semaphore semaphore = mock(Semaphore.class);
        serverPermit.setSemaphore(semaphore);
        serverPermit.close();
        verify(semaphore).release();
    }

    @Test
    public void shouldGetDefaultSemaphore() {
        assertNotNull(serverPermit.getSemaphore());
    }

    @Test
    public void shouldSetSuppliedSemaphore() {
        Semaphore semaphore = mock(Semaphore.class);
        serverPermit.setSemaphore(semaphore);
        assertEquals(semaphore, serverPermit.getSemaphore());
    }
    
    @Test
    public void shouldReleaseSemaphorePermitWhenUsingTryWith() throws Exception {
        Semaphore semaphore = mock(Semaphore.class);
        try(final ServerPermit sp = new ServerPermit()){
            sp.setSemaphore(semaphore);
        }
        verify(semaphore).release();
    }
    
    @Test
    public void shouldReleaseSemaphorePermitWhenUsingTryWithAndExceptionIsThrown() throws Exception {
        Semaphore semaphore = mock(Semaphore.class);
        try(final ServerPermit sp = new ServerPermit()){
            sp.setSemaphore(semaphore);
            throw new RuntimeException("Testing Permit closing"); 
        }
        catch(RuntimeException e){
            verify(semaphore).release();
        }
    }
    
}

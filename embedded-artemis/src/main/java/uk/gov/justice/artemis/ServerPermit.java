package uk.gov.justice.artemis;

import java.util.concurrent.Semaphore;

/**
 * 
 * Explicit Permit for restricting access on EmbeddedArtemisServer <br>
 * <br>
 * Current restricted operations include starting and stopping <br>
 * an instance of the EmbeddedJMSServer in a mutually exclusive manner. <br>
 * 
 * Example usage <br>
 * <code>
 * 
 * try (final ServerPermit sp = serverPermit.acquire()) {
 *     jmsServer.start();
 * }
 * </code>
 * 
 * @see EmbeddedArtemisServer
 * @see EmbeddedJMSServer
 *
 */
public class ServerPermit implements AutoCloseable {

    private Semaphore semaphore;

    /**
     * Create a semaphore with a single permit for exclusion
     */
    public ServerPermit() {
        semaphore = new Semaphore(1, true);
    }

    /**
     * Acquire a permit
     * 
     * @return ServerPermit
     * @throws InterruptedException
     */
    public ServerPermit acquire() throws InterruptedException {
        semaphore.acquire();
        return this;
    }

    /**
     * Release a permit
     */
    @Override
    public void close() throws Exception {
        semaphore.release();
    }

    /**
     * Get a reference to Semaphore
     * 
     * @return Semaphore
     */
    public Semaphore getSemaphore() {
        return semaphore;
    }

    /**
     * Set a reference to Semaphore
     * 
     * @param Semaphore
     */
    public ServerPermit setSemaphore(final Semaphore semaphore) {
        this.semaphore = semaphore;
        return this;
    }
}

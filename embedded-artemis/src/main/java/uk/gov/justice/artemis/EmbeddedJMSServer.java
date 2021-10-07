package uk.gov.justice.artemis;

import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;

/**
 * An embedded JMS server with state checks for operations.
 * 
 * @see org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS
 */
public class EmbeddedJMSServer {

    /**
     * @see org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS
     */
    private EmbeddedJMS jmsServer;

    /**
     * Capture the state of the server
     */
    private volatile boolean initialised;

    /**
     * Start the server if its not running
     * 
     * @throws Exception if unsuccessful
     */
    public void start() throws Exception {

        if (initialised) {
            return;
        }

        jmsServer.start();

        initialised = true;
    }

    /**
     * Stop the server if its running
     * @return true if shutdown succeeded
     * @throws Exception if unsuccessful
     */
    public boolean stop() throws Exception {

        if (initialised) {
            jmsServer.stop();

            initialised = false;
        }

        return initialised;
    }

    /**
     * Get a reference to EmbeddedJMS
     * 
     * @return EmbeddedJMS
     */
    public EmbeddedJMS getJmsServer() {
        return jmsServer;
    }

    /**
     * Set a reference to EmbeddedJMS
     *
     * @param jmsServer An embedded JmsServer
     * @return EmbeddedJMS
     */
    public EmbeddedJMSServer setJmsServer(final EmbeddedJMS jmsServer) {
        this.jmsServer = jmsServer;
        return this;
    }

    /**
     * Get the initialisation flag
     * 
     * @return boolean
     */
    public boolean isInitialised() {
        return initialised;
    }

    /**
     * Set the initialisation flag
     * 
     * @param initialised flag
     */
    public void setInitialised(final boolean initialised) {
        this.initialised = initialised;
    }

}
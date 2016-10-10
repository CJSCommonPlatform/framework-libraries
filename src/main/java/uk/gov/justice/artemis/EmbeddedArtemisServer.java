package uk.gov.justice.artemis;

import static uk.gov.justice.artemis.EmbeddedArtemisInitializer.initialize;

import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * An embedded Artemis server that should be used <b> only for testing </b>.
 *
 * Example usage given below <br>
 * <code>
 *     &#64;BeforeClass
 *     public static void beforeClass() {
 *         try{
 *             EmbeddedArtemisServer.startServer();
 *         }catch(Throwable e){
 *             LOG.error("EmbeddedArtemisServer start: ", e);
 *             fail(e.getMessage());
 *         }
 *     }
 * 
 *     &#64;AfterClass
 *     public static void afterClass() {
 *         try{
 *             EmbeddedArtemisServer.stopServer();
 *         }catch(Exception e){
 *             LOG.error("EmbeddedArtemisServer stop: ", e);
 *         }
 *     }
 * 
 * </code>
 * 
 */
public final class EmbeddedArtemisServer {

    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedArtemisServer.class);

    /**
     * Shutdown hook to ensure that all resources are tidy before exit
     */
    static {
        Runtime.getRuntime().addShutdownHook(getShutDownHook());
    }

    /**
     * Create an instance of the EmbeddedJMSServer
     */
    private static EmbeddedJMSServer jmsServer =
                    new EmbeddedJMSServer().setJmsServer(initialize(new EmbeddedJMS()));

    /**
     * Create an explicit permit for invoking methods on the server.
     */
    private static ServerPermit serverPermit = new ServerPermit();

    /**
     * Private constructor to avoid instantiating the utility class
     */
    private EmbeddedArtemisServer() {}

    /**
     * Start an embedded Artemis server using a broker.xml from a projects <br>
     * class path
     * 
     * @param args not expected
     * @throws Exception while starting the server.
     */
    public static void main(String args[]) throws Exception {
        startServer();
    }

    /**
     * Start the server after acquiring a permit
     * 
     * @throws Exception if unsuccessful
     */
    public static final void startServer() throws Exception {
        try (final ServerPermit sp = serverPermit.acquire()) {
            jmsServer.start();
        }
    }

    /**
     * Stop the server after acquiring a permit
     * 
     * @throws Exception if unsuccessful
     */
    public static final boolean stopServer() {
        try (final ServerPermit sp = serverPermit.acquire()) {
            return jmsServer.stop();
        } catch (Exception e) {
            LOG.error("EmbeddedArtemisServer", e);
        }
        return false;
    }

    /**
     * Get the shutdown hook for stopping the server
     * 
     * @return Thread
     */
    public static Thread getShutDownHook() {
        return new Thread(() -> {
            stopServer();
        }, "EmbeddedArtemisServerRuntimeHook");
    }

    /**
     * Get a reference to EmbeddedJMSServer
     * 
     * @return EmbeddedJMSServer
     */
    public static EmbeddedJMSServer getJmsServer() {
        return jmsServer;
    }

    /**
     * Set a reference to EmbeddedJMSServer
     * 
     * @param EmbeddedJMSServer
     * @see EmbeddedJMSServer
     */
    public static void setJmsServer(final EmbeddedJMSServer jmsServer) {
        EmbeddedArtemisServer.jmsServer = jmsServer;
    }

    /**
     * Set a reference to EmbeddedJMS
     * 
     * @return EmbeddedJMS
     * @see EmbeddedJMS
     */
    public static void setEmbeddedJms(final EmbeddedJMS embeddedJms) {
        jmsServer.setJmsServer(embeddedJms);
    }

    /**
     * Get a reference to EmbeddedJMS
     * 
     * @return EmbeddedJMS
     */
    public static EmbeddedJMS getEmbeddedJms() {
        return jmsServer.getJmsServer();
    }

    /**
     * Get a reference to ServerPermit
     * 
     * @return ServerPermit
     */
    public static ServerPermit getServerPermit() {
        return serverPermit;
    }

    /**
     * Set a reference to ServerPermit
     * 
     * @param ServerPermit
     */
    public static void setServerPermit(final ServerPermit serverPermit) {
        EmbeddedArtemisServer.serverPermit = serverPermit;
    }
}

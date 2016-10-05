package uk.gov.justice.artemis;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.apache.activemq.artemis.core.security.CheckType;
import org.apache.activemq.artemis.core.security.Role;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.apache.activemq.artemis.spi.core.security.ActiveMQSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * An embedded Artemis server that should be used only for testing.
 *
 */
public final class EmbeddedArtemisServer {

    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedArtemisServer.class);

    static {
        Runtime.getRuntime().addShutdownHook(getShutDownHook());
    }

    private static EmbeddedJMS jmsServer;

    private static final Semaphore serverPermit = new Semaphore(1, true);

    private static volatile boolean initialised;

    private EmbeddedArtemisServer() {}

    public static void main(String args[]) throws Exception {

        startServer();
    }

    /**
     * Start the server using a broker.xml from your project classpath.
     * 
     * @throws Exception
     */
    public static final void startServer() throws Exception {

        serverPermit.acquire();
        try {

            if (initialised) {
                return;
            }

            checkLoggers();

            if (Objects.isNull(jmsServer)) {
                setJmsServer(new EmbeddedJMS());
            }

            jmsServer.setConfigResourcePath("broker.xml");

            // override the security manager to cater for multiple test configurations
            // users and passwords
            jmsServer.setSecurityManager(getSecurityManager());

            jmsServer.start();

            initialised = true;

        } finally {
            serverPermit.release();
        }
    }

    /**
     * Stop the server
     * 
     * @throws Exception
     */
    public static final void stopServer() throws Exception {
        serverPermit.acquire();
        try {
            if (initialised) {
                jmsServer.stop();
                initialised = false;
            }
        } finally {
            serverPermit.release();
        }
    }

    public static EmbeddedJMS getJmsServer() {
        return jmsServer;
    }

    public static void setJmsServer(final EmbeddedJMS jmsServer) {
        EmbeddedArtemisServer.jmsServer = jmsServer;
    }

    private static void checkLoggers() {
        // ensure that the loggers are in the classpath and loaded
        org.apache.activemq.artemis.core.server.ActiveMQServerLogger.class.getCanonicalName();
        org.apache.activemq.artemis.core.server.ActiveMQServerLogger_$logger.class
                        .getCanonicalName();
    }

    private static ActiveMQSecurityManager getSecurityManager() {
        return new ActiveMQSecurityManager() {
            @Override
            public boolean validateUser(final String user, final String password) {
                return true;
            }

            @Override
            public boolean validateUserAndRole(final String user, final String password,
                            final Set<Role> roles, final CheckType checkType) {
                return true;
            }
        };
    }

    private static Thread getShutDownHook() {
        return new Thread(() -> {
            try {
                stopServer();
            } catch (Exception e) {
                LOG.error("EmbeddedArtemisServerRuntimeHook", e);
            }
        }, "EmbeddedArtemisServerRuntimeHook");
    }
}

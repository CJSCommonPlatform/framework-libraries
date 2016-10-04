package uk.gov.justice.artemis;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.apache.activemq.artemis.core.security.CheckType;
import org.apache.activemq.artemis.core.security.Role;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.apache.activemq.artemis.spi.core.security.ActiveMQSecurityManager;

/**
 * 
 * An embedded Artemis server that can be used for testing
 *
 */
public final class EmbeddedArtemisServer {

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    serverPermit.acquire();
                    try {
                        stopServer();
                    } finally {
                        serverPermit.release();
                    }
                } catch (Exception e) {
                }
            }
        }, "EmbeddedArtemisServerRuntimeHook"));
    }

    private static EmbeddedJMS jmsServer;

    private static final Semaphore serverPermit = new Semaphore(1, true);

    private static volatile boolean initialised;

    private EmbeddedArtemisServer() {}

    public static void main(String args[]) throws Exception {

        startServer();
    }

    public static final void startServer() throws Exception {

        serverPermit.acquire();
        try {
            if (initialised) {
                return;
            }
            // ensure that the loggers are in the classpath and loaded
            org.apache.activemq.artemis.core.server.ActiveMQServerLogger.class.getCanonicalName();
            org.apache.activemq.artemis.core.server.ActiveMQServerLogger_$logger.class
                            .getCanonicalName();

            if (Objects.isNull(jmsServer)) {
                setJmsServer(new EmbeddedJMS());
            }

            jmsServer.setConfigResourcePath("broker.xml");

            // override the security manager to cater for multiple test configurations
            // users and passwords
            jmsServer.setSecurityManager(new ActiveMQSecurityManager() {
                @Override
                public boolean validateUser(String user, String password) {
                    return true;
                }

                @Override
                public boolean validateUserAndRole(String user, String password, Set<Role> roles,
                                CheckType checkType) {
                    return true;
                }
            });

            jmsServer.start();

            initialised = true;

        } finally {
            serverPermit.release();
        }
    }

    public static synchronized final void stopServer() throws Exception {
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

}

package uk.gov.justice.artemis;

import java.util.Set;

import org.apache.activemq.artemis.core.security.CheckType;
import org.apache.activemq.artemis.core.security.Role;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.apache.activemq.artemis.spi.core.security.ActiveMQSecurityManager;

/**
 * 
 * An utility class for initialising an EmbeddedJMS instance <br>
 * with certain checks and configuration objects.
 * 
 * @see org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS
 *
 */
public final class EmbeddedArtemisInitializer {

    /**
     * Private constructor to avoid instantiating the utility class.
     */
    private EmbeddedArtemisInitializer() {}

    /**
     * Initialise an EmbeddedJMS with a configuration XML and security manager.
     * 
     * @param jmsServer to be initialised
     * @return EmbeddedJMS
     * @see org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS
     */
    public static EmbeddedJMS initialize(final EmbeddedJMS jmsServer) {
        jmsServer.setConfigResourcePath("broker.xml");
        jmsServer.setSecurityManager(getSecurityManager());
        return jmsServer;
    }

    /**
     * Ensure that the ActiveMQServerLogger loggers are in the class path and loaded correctly. <br>
     * <br>
     * The ActiveMQServerLogger_$logger classes are generated dynamically and bundled in the <br>
     * executable jars.<br>
     * Having ActiveMQ jars without the bundled _$logger in the class path lead to <br>
     * spurious errors that we want to avoid by having an initialisation check.
     */
    public static void checkLoggers() {
        org.apache.activemq.artemis.core.server.ActiveMQServerLogger.class.getCanonicalName();
        org.apache.activemq.artemis.core.server.ActiveMQServerLogger_$logger.class
                        .getCanonicalName();
    }

    /**
     * An overridden security manager to cater for multiple test configuration <br>
     * users and passwords. <br>
     * <br>
     * The returned security manager does not check for user passwords or user roles. <br>
     * <br>
     * Different project setups use different user/passwords in connections<br>
     * which can quite conveniently be handled by this SecurityManager.
     * <br>
     * The EmbeddedArtemisServer should only be used <b>for testing</b>.
     * 
     * @return ActiveMQSecurityManager
     * @see ActiveMQSecurityManager
     */
    public static ActiveMQSecurityManager getSecurityManager() {
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
}
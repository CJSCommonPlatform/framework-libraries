package uk.gov.justice.artemis;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.apache.activemq.artemis.core.security.CheckType;
import org.apache.activemq.artemis.spi.core.security.ActiveMQSecurityManager;
import org.junit.jupiter.api.Test;

public class EmbeddedArtemisInitializerTest {


    @Test
    public void shouldTestActiveMQSecurityManagerAlwaysTrue() {
        ActiveMQSecurityManager activeMQSecurityManager =
                        EmbeddedArtemisInitializer.getSecurityManager();
        assertNotNull(activeMQSecurityManager);
        assertTrue(activeMQSecurityManager.validateUser("user", "password"));
        assertTrue(activeMQSecurityManager.validateUserAndRole("user", "password",
                        Collections.emptySet(), CheckType.SEND));
    }

    @Test
    public void shouldTestLoggersClassesAreAvailable(){
        EmbeddedArtemisInitializer.checkLoggers();
    }
    
}

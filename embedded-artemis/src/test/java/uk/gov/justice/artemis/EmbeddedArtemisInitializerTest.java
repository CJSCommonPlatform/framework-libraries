package uk.gov.justice.artemis;

import static net.trajano.commons.testing.UtilityClassTestUtil.assertUtilityClassWellDefined;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.apache.activemq.artemis.core.security.CheckType;
import org.apache.activemq.artemis.spi.core.security.ActiveMQSecurityManager;
import org.junit.jupiter.api.Test;

public class EmbeddedArtemisInitializerTest {

    @Test
    public void shouldBeWellDefinedUtilityClass() {
        assertUtilityClassWellDefined(EmbeddedArtemisInitializer.class);
    }

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

package uk.gov.justice.services.test.utils.core.messaging;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static uk.gov.justice.services.test.utils.core.messaging.JsonObjects.jsonBuilderFactory;
import static uk.gov.justice.services.test.utils.core.messaging.JsonObjects.jsonReaderFactory;

class JsonObjectsTest {

    @Test
    public void shouldJsonObjectsCacheProviders() {
        assertNotNull(jsonBuilderFactory);
        // parsson returns null from getConfigInUse() when no config was set; glassfish returned empty map — both mean no config
        assertTrue(jsonBuilderFactory.getConfigInUse() == null || jsonBuilderFactory.getConfigInUse().isEmpty());
        assertNotNull(jsonReaderFactory);
        assertTrue(jsonReaderFactory.getConfigInUse() == null || jsonReaderFactory.getConfigInUse().isEmpty());
    }

}
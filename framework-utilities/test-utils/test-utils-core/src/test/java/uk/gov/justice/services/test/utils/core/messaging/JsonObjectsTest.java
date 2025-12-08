package uk.gov.justice.services.test.utils.core.messaging;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static uk.gov.justice.services.test.utils.core.messaging.JsonObjects.jsonBuilderFactory;
import static uk.gov.justice.services.test.utils.core.messaging.JsonObjects.jsonReaderFactory;

class JsonObjectsTest {

    @Test
    public void shouldJsonObjectsCacheProviders() {
        assertNotNull(jsonBuilderFactory);
        assertTrue(jsonBuilderFactory.getConfigInUse().isEmpty());
        assertNotNull(jsonReaderFactory);
        assertTrue(jsonReaderFactory.getConfigInUse().isEmpty());
    }

}
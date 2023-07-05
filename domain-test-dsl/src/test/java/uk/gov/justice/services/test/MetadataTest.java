package uk.gov.justice.services.test;

import static org.junit.Assert.assertNull;

import uk.gov.justice.services.test.domain.Metadata;

import org.junit.jupiter.api.Test;

public class MetadataTest {

    @Test
    public void testEmptyCreation() {
        final Metadata metadata = new Metadata();
        assertNull(metadata.getName());
    }

}

package uk.gov.justice.schema.catalog.client;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import uk.gov.justice.schema.catalog.RawCatalog;

import java.lang.reflect.Field;

import org.everit.json.schema.loader.SchemaClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SchemaClientFactoryTest {

    @InjectMocks
    private SchemaClientFactory schemaClientFactory;

    @Test
    public void shouldCreateANewLocalFileSystemSchemaClient() throws Exception {

        final RawCatalog rawCatalog = mock(RawCatalog.class);

        final SchemaClient schemaClient = schemaClientFactory.create(rawCatalog);

        assertThat(schemaClient, is(instanceOf(LocalFileSystemSchemaClient.class)));

        final Field rawCatalogField = schemaClient.getClass().getDeclaredField("rawCatalog");
        rawCatalogField.setAccessible(true);

        assertThat(rawCatalogField.get(schemaClient), is(rawCatalog));
    }
}

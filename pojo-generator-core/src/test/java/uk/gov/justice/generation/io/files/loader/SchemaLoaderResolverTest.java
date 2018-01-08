package uk.gov.justice.generation.io.files.loader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.JsonToSchemaConverter;
import uk.gov.justice.schema.catalog.RawCatalog;
import uk.gov.justice.schema.catalog.client.SchemaClientFactory;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchemaLoaderResolverTest {

    @Mock
    private RawCatalog rawCatalog;

    @Mock
    private SchemaClientFactory schemaClientFactory;

    @Mock
    private JsonToSchemaConverter jsonStringToSchemaConverter;

    @InjectMocks
    private SchemaLoaderResolver schemaLoaderResolver;

    @Test
    public void shouldResolveSchema() {
        final Schema schema = mock(Schema.class);
        final JSONObject jsonSchema = mock(JSONObject.class);
        final SchemaClient schemaClient = mock(SchemaClient.class);

        when(schemaClientFactory.create(rawCatalog)).thenReturn(schemaClient);
        when(jsonStringToSchemaConverter.convert(jsonSchema, schemaClient)).thenReturn(schema);

        final Schema resultSchema = schemaLoaderResolver.loadSchema(jsonSchema);

        assertThat(resultSchema, is(schema));
    }
}
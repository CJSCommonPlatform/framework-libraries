package uk.gov.justice.schema.catalog;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.client.LocalFileSystemSchemaClient;
import uk.gov.justice.schema.catalog.client.SchemaClientFactory;

import org.everit.json.schema.Schema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class CatalogTest {

    @Mock
    private RawCatalog rawCatalog;

    @Mock
    private SchemaClientFactory schemaClientFactory;

    @Mock
    private JsonToSchemaConverter jsonToSchemaConverter;

    @InjectMocks
    private Catalog catalog;

    @Test
    public void shouldLoadTheRawJsonSchemaAndConvertToAFullSchemaObject() throws Exception {

        final String schemaId = "schemaId";
        final String rawJsonSchema = "raw json schema";

        final LocalFileSystemSchemaClient localFileSystemSchemaClient = mock(LocalFileSystemSchemaClient.class);
        final Schema schema = mock(Schema.class);

        when(rawCatalog.getRawJsonSchema(schemaId)).thenReturn(of(rawJsonSchema));
        when(schemaClientFactory.create(rawCatalog)).thenReturn(localFileSystemSchemaClient);
        when(jsonToSchemaConverter.convert(rawJsonSchema, localFileSystemSchemaClient)).thenReturn(schema);

        assertThat(catalog.getSchema(schemaId), is(of(schema)));
    }

    @Test
    public void shouldReturnEmptyIfNoRawSchemaJsonFound() throws Exception {

        final String schemaId = "schemaId";

        when(rawCatalog.getRawJsonSchema(schemaId)).thenReturn(empty());

        assertThat(catalog.getSchema(schemaId), is(empty()));
    }
}

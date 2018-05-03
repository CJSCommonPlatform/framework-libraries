package uk.gov.justice.generation.io.files.resolver;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.io.files.loader.Resource;

import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchemaResolverTest {

    @Mock
    private SchemaCatalogResolver schemaCatalogResolver;

    @InjectMocks
    private SchemaResolver schemaResolver;

    @Test
    public void shouldLoadObjectSchema() throws Exception {

        final Resource resource = mock(Resource.class);
        final JSONObject jsonObject = mock(JSONObject.class);
        final Schema schema = mock(Schema.class);

        when(resource.asJsonObject()).thenReturn(jsonObject);
        when(schemaCatalogResolver.loadSchema(jsonObject)).thenReturn(schema);
        when(jsonObject.has("id")).thenReturn(true);

        final Schema resultSchema = schemaResolver.resolve(resource);

        assertThat(resultSchema, is(schema));
    }

    @Test
    public void shouldThrowExceptionIfSchemaFileDoesNotHaveAnId() throws Exception {

        final Resource resource = mock(Resource.class);
        final JSONObject jsonObject = mock(JSONObject.class);
        final Schema schema = mock(Schema.class);

        when(resource.asJsonObject()).thenReturn(jsonObject);
        when(schemaCatalogResolver.loadSchema(jsonObject)).thenReturn(schema);

        try {
            schemaResolver.resolve(resource);
            fail();
        } catch (final SchemaResolverException expected) {
            assertThat(expected.getMessage(), is("Missing id in Schema. Unable to resolve"));
        }
    }

    @Test
    public void shouldCreateSchemaLoaderExceptionWithMessage() throws Exception {
        final SchemaResolverException schemaResolverException = new SchemaResolverException("test message");
        assertThat(schemaResolverException.getMessage(), is("test message"));
    }

    @Test
    public void shouldCreateSchemaLoaderExceptionWithMessageAndCause() throws Exception {
        final Exception cause = mock(Exception.class);
        final SchemaResolverException schemaResolverException = new SchemaResolverException("test message", cause);

        assertThat(schemaResolverException.getMessage(), is("test message"));
        assertThat(schemaResolverException.getCause(), is(cause));
    }
}

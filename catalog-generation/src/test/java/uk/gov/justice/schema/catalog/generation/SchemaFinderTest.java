package uk.gov.justice.schema.catalog.generation;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.net.URI;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchemaFinderTest {

    @Spy @SuppressWarnings("unused")
    private final UrlConverter urlConverter = new UrlConverter();

    @InjectMocks
    private SchemaFinder schemaFinder;

    @Test
    public void shouldFindAllJsonFilesOnTheClasspathAtTheCorrectLocation() throws Exception {

        final List<URI> schemas = schemaFinder.listSchemas();

        assertThat(schemas.get(0).toString(), endsWith("raml/json/schema/context/person.json"));
        assertThat(schemas.get(1).toString(), endsWith("raml/json/schema/standards/address.json"));
        assertThat(schemas.get(2).toString(), endsWith("raml/json/schema/standards/complex_address.json"));
        assertThat(schemas.get(3).toString(), endsWith("raml/json/schema/standards/defendant.json"));
    }
}

package uk.gov.justice.schema.catalog;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import uk.gov.justice.schema.catalog.util.UriResolver;
import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class SchemaResolverTest {

    @Spy
    @SuppressWarnings("unused")
    private final UrlConverter urlConverter = new UrlConverter();

    @Spy
    @SuppressWarnings("unused")
    private final UriResolver uriResolver = new UriResolver();

    @InjectMocks
    private SchemaResolver schemaResolver;

    @Test
    public void shouldResolveTheLocationToAUrlUsingTheCatalogUrlAndBaseLocation() throws Exception {

        final URI absoluteCatalogUri = new URI("file:/src/main/resources/META-INF/catalog-file.json");

        final String schemaFileLocation = "some/path/to/schema.json";
        final Optional<String> baseLocation = of("base/location/");

        final URL resolvedUri = schemaResolver.resolve(absoluteCatalogUri, schemaFileLocation, baseLocation);

        assertThat(resolvedUri.toString(), is("file:/src/main/resources/base/location/some/path/to/schema.json"));
    }

    @Test
    public void shouldResolveTheLocationToAUrlUsingTheCatalogUrlAndAnEmptyBaseLocation() throws Exception {

        final URI absoluteCatalogUri = new URI("file:/src/main/resources/META-INF/catalog-file.json");

        final String schemaFileLocation = "some/path/to/schema.json";
        final Optional<String> baseLocation = empty();

        final URL resolvedUri = schemaResolver.resolve(absoluteCatalogUri, schemaFileLocation, baseLocation);

        assertThat(resolvedUri.toString(), is("file:/src/main/resources/some/path/to/schema.json"));
    }

    @Test
    public void shouldFailIfResolvingTheUrlThrowsAURISyntaxException() throws Exception {

        final URI catalogUri = new URI("file:/src/main/resources/META-INF/catalog-file.json");

        final String fileLocation = "some/path/to/schema.json";
        final Optional<String> baseLocation = of("this path is silly");

        final SchemaCatalogException expected = assertThrows(
                SchemaCatalogException.class,
                () -> schemaResolver.resolve(catalogUri, fileLocation, baseLocation));

        assertThat(expected.getCause(), is(instanceOf(URISyntaxException.class)));
        assertThat(expected.getMessage(), is("Failed to resolve 'file:/src/main/resources/META-INF/catalog-file.json', to file location 'some/path/to/schema.json', with base location 'this path is silly'"));
    }
}

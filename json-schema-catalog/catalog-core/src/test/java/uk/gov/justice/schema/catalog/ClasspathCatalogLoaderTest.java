package uk.gov.justice.schema.catalog;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.domain.Catalog;
import uk.gov.justice.schema.catalog.util.ClasspathResourceLoader;
import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClasspathCatalogLoaderTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ClasspathResourceLoader classpathResourceLoader;

    @SuppressWarnings("unused")
    @Spy
    private final CatalogContext catalogContext = new CatalogContext();

    @Mock
    private UrlConverter urlConverter;

    @InjectMocks
    private ClasspathCatalogLoader classpathCatalogLoader;

    @Test
    public void shouldThrowExceptionIfLoadingFileThrowsIOException() throws Exception {

        final IOException ioException = new IOException("Ooops");
        final URL url = new URL("file://src/code/my-file.txt");
        final URI uri = url.toURI();

        when(classpathResourceLoader.getResources("META-INF/schema_catalog.json")).thenReturn(singletonList(url));
        when(urlConverter.toUri(url)).thenReturn(uri);
        when(objectMapper.readValue(url, Catalog.class)).thenThrow(ioException);

        try {
            classpathCatalogLoader.getCatalogs();
            fail();
        } catch (final SchemaCatalogException expected) {
            assertThat(expected.getCause(), is(ioException));
            assertThat(expected.getMessage(), is("Failed to convert to json loaded from 'file://src/code/my-file.txt' to a Catalog pojo"));
        }
    }

    @Test
    public void shouldThrowExceptionIfLoadingResourcesThrowsIOException() throws Exception {

        final IOException ioException = new IOException("Ooops");

        when(classpathResourceLoader.getResources("META-INF/schema_catalog.json")).thenThrow(ioException);

        final Exception expected = assertThrows(
                Exception.class,
                () -> classpathCatalogLoader.getCatalogs());
        assertThat(expected.getCause(), is(ioException));
        assertThat(expected.getMessage(), startsWith("Failed to load the catalogs from the classpath for location 'META-INF/schema_catalog.json'"));
    }

    @Test
    public void shouldNotThrowExceptionIfDuplicateKeyAdded() throws Exception {

        final URL url = new URL("file://src/code/my-file.txt");
        final URI uri = url.toURI();

        when(classpathResourceLoader.getResources("META-INF/schema_catalog.json")).thenReturn(asList(url, url));
        when(urlConverter.toUri(url)).thenReturn(uri);
        when(objectMapper.readValue(url, Catalog.class)).thenReturn(mock(Catalog.class));

        try {
            classpathCatalogLoader.getCatalogs();
        } catch (final Exception notExpected) {
            fail("Should not have thrown any exception");
        }

    }
}

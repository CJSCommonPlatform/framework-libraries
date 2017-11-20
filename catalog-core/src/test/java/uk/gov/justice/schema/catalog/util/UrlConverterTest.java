package uk.gov.justice.schema.catalog.util;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.schema.catalog.SchemaCatalogException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class UrlConverterTest {

    @InjectMocks
    private UrlConverter urlConverter;

    @Test
    public void shouldConvertAAurlToAnUri() throws Exception {

        final URL url = new URL("file://src/main/fred.txt");
        final URI uri = urlConverter.toUri(url);

        assertThat(uri.toString(), is("file://src/main/fred.txt"));
    }

    @Test
    public void shouldConvertAnUrlToAnUri() throws Exception {

        final URI uri = new URI("file://src/main/fred.txt");
        final URL url = urlConverter.toUrl(uri);

        assertThat(url.toString(), is("file://src/main/fred.txt"));
    }

    @Test
    public void shouldConvertAUriStringToAUriObject() throws Exception {

        final String uri = "file://src/main/fred.txt";

        assertThat(urlConverter.toUri(uri).toString(), is(uri));
    }

    @Test
    public void shouldFailIfConvertingAStringToAUriThrowsAURISyntaxException() throws Exception {
        final String uri = "this is not a uri";

        try {
            urlConverter.toUri(uri);
            fail();
        } catch (final SchemaCatalogException expected) {
            assertThat(expected.getCause(), is(instanceOf(URISyntaxException.class)));
            assertThat(expected.getMessage(), is("Failed to convert URI 'this is not a uri' to URL"));
        }
    }
}

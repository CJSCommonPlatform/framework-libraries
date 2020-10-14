package uk.gov.justice.schema.catalog.util;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.schema.catalog.SchemaCatalogException;

import java.net.MalformedURLException;
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

    @Test
    public void shouldFailIfConvertingAUrlToAUriThrowsAURISyntaxException() throws Exception {

        final URL url = new URL("file:/this is not a uri");
        try {
            urlConverter.toUri(url);
            fail();
        } catch (final SchemaCatalogException expected) {
            assertThat(expected.getCause(), is(instanceOf(URISyntaxException.class)));
            assertThat(expected.getMessage(), is("Failed to convert URL 'file:/this is not a uri' to URI"));
        }
    }

    @Test
    public void shouldFailIfConvertingAUriToAUrlThrowsAMalformedURLException() throws Exception {

        final URI uri = new URI("silly:/this-is-not-a-url");
        try {
            urlConverter.toUrl(uri);
            fail();
        } catch (final SchemaCatalogException expected) {
            assertThat(expected.getCause(), is(instanceOf(MalformedURLException.class)));
            assertThat(expected.getMessage(), is("Failed to convert URI 'silly:/this-is-not-a-url' to URL"));
        }
    }
}

package uk.gov.justice.schema.catalog.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.URI;
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
}

package uk.gov.justice.generation.io.files.loader;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ResourceTest {

    @Mock
    private Path basePath;

    @Mock
    private Path resourcePath;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private InputStreamToJsonObjectConverter inputStreamToJsonObjectConverter;

    @Test
    public void shouldReturnResourceAsJsonObject() throws Exception {

        final InputStream inputStream = mock(InputStream.class);
        final JSONObject jsonObject = mock(JSONObject.class);

        when(resourceLoader.loadFrom(basePath, resourcePath)).thenReturn(inputStream);
        when(inputStreamToJsonObjectConverter.toJsonObject(inputStream)).thenReturn(jsonObject);

        final Resource resource = new Resource(basePath, resourcePath, resourceLoader, inputStreamToJsonObjectConverter);
        final JSONObject result = resource.asJsonObject();

        assertThat(result, is(jsonObject));
    }

    @Test
    public void shouldThrowExceptionIfResourceFailsToLoad() throws Exception {
        final InputStream inputStream = mock(InputStream.class);

        when(resourceLoader.loadFrom(basePath, resourcePath)).thenReturn(inputStream);
        when(inputStreamToJsonObjectConverter.toJsonObject(inputStream)).thenThrow(new IOException("Failed"));
        when(resourcePath.toString()).thenReturn("Resource");

        final Resource resource = new Resource(basePath, resourcePath, resourceLoader, inputStreamToJsonObjectConverter);

        try {
            resource.asJsonObject();
            fail();
        } catch (final ResourceLoadingException ex) {
            assertThat(ex.getMessage(), is("Unable to load resource: Resource"));
            assertThat(ex.getCause(), is(instanceOf(IOException.class)));
        }
    }
}
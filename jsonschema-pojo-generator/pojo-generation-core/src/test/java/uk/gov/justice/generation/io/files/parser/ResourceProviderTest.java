package uk.gov.justice.generation.io.files.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.generation.io.files.loader.ClasspathResourceLoader;
import uk.gov.justice.generation.io.files.loader.InputStreamToJsonObjectConverter;
import uk.gov.justice.generation.io.files.loader.Resource;
import uk.gov.justice.generation.io.files.loader.ResourceLoader;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ResourceProviderTest {

    @SuppressWarnings("unused")
    @Spy
    private InputStreamToJsonObjectConverter inputStreamToJsonObjectConverter = new InputStreamToJsonObjectConverter();

    @InjectMocks
    private ResourceProvider resourceProvider;

    @Test
    public void shouldLoadAResource() throws Exception {

        final ResourceLoader resourceLoader = new ClasspathResourceLoader();

        final Path basePath = Paths.get("CLASSPATH");
        final Path resourcePath = Paths.get("json/person.json");

        final Resource resource = resourceProvider.getResource(basePath, resourcePath, resourceLoader);

        final JSONObject jsonObject = resource.asJsonObject();

        assertThat(jsonObject.get("firstName"), is("Fred"));
        assertThat(jsonObject.get("lastName"), is("Bloggs"));
    }
}

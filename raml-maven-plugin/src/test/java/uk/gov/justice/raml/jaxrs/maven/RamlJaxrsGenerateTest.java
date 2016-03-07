package uk.gov.justice.raml.jaxrs.maven;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by david on 04/03/16.
 */
public class RamlJaxrsGenerateTest extends BetterAbstractMojoTestCase {

    public void testGenerateMojoSuccess() throws Exception {
        File pom = getTestFile( "src/test/resources/generate/pom.xml" );

        RamlJaxrsCodegenMojo mojo = (RamlJaxrsCodegenMojo) lookupConfiguredMojo(pom, "generate");

        assertNotNull(mojo);
        mojo.execute();

        Path outputPath = Paths.get(getBasedir(), "src", "test", "resources", "generate", "target", "generated-sources", "example.json");
        JsonReader jsonReader = Json.createReader(Files.newInputStream(outputPath));
        JsonObject output = jsonReader.readObject();

        Path expectedSourceDirectory = Paths.get(getBasedir(), "src", "test", "resources", "generate", "src", "raml");
        Path expectedOutputDirectory = Paths.get(getBasedir(), "src", "test", "resources", "generate", "target", "generated-sources");
        assertThat(output.getString("raml"), equalTo("#%RAML 0.8" + System.getProperty("line.separator")));
        assertThat(output.getJsonObject("configuration").getString("sourceDirectory"), equalTo(expectedSourceDirectory.toString()));
        assertThat(output.getJsonObject("configuration").getString("outputDirectory"), equalTo(expectedOutputDirectory.toString()));
        assertThat(output.getJsonObject("configuration").getString("basePackageName"), equalTo("uk.gov.justice.api"));
    }
}

package uk.gov.justice.raml.jaxrs.maven;

import org.apache.maven.plugin.MojoExecutionException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.newInputStream;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Maven plugin harness test for the {@link GenerateMojo} class.
 */
public class GenerateMojoTest extends BetterAbstractMojoTestCase {


    public void testShouldGenerateFromValidRaml() throws Exception {
        File pom = getTestFile( "src/test/resources/generate/pom.xml" );

        GenerateMojo mojo = (GenerateMojo) lookupConfiguredMojo(pom, "generate");

        mojo.execute();

        Path outputPath = Paths.get(project.getBasedir().toString(), "target", "generated-sources", DummyGenerator.OUTPUT_FILE);
        JsonReader jsonReader = Json.createReader(newInputStream(outputPath));
        JsonObject output = jsonReader.readObject();

        Path expectedSourceDirectory = Paths.get(project.getBasedir().toString(), "src", "raml");
        Path expectedOutputDirectory = Paths.get(project.getBasedir().toString(), "target", "generated-sources");
        assertThat(output.getJsonArray("raml").getString(0), equalTo("#%RAML 0.8"));
        assertThat(output.getJsonArray("raml").size(), equalTo(1));
        assertThat(output.getJsonObject("configuration").getString("sourceDirectory"), equalTo(expectedSourceDirectory.toString()));
        assertThat(output.getJsonObject("configuration").getString("outputDirectory"), equalTo(expectedOutputDirectory.toString()));
        assertThat(output.getJsonObject("configuration").getString("basePackageName"), equalTo("uk.gov.justice.api"));
        assertThat(output.getJsonObject("configuration").getJsonObject("generatorProperties").getString("property1"), equalTo("propertyValueABC"));
        assertThat(output.getJsonObject("configuration").getJsonObject("generatorProperties").getString("property2"), equalTo("propertyValueDDD"));


        assertThat((project.getCompileSourceRoots()), hasItem(outputPath.getParent().toAbsolutePath().toString()));
        assertThat((project.getTestCompileSourceRoots()), hasItem(outputPath.getParent().toAbsolutePath().toString()));
    }

    public void testShouldNotProcessInvalidRamlFile() throws Exception {

        File pom = getTestFile( "src/test/resources/invalid-raml/pom.xml" );

        GenerateMojo mojo = (GenerateMojo) lookupConfiguredMojo(pom, "generate");

        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException e) {

        }
    }

    public void testShouldNotProcessNonRAMLFile() throws Exception {

        Path outputPath = Paths.get(getBasedir(), "src", "test", "resources", "generate", "target", "generated-sources");

        File pom = getTestFile( "src/test/resources/non-raml/pom.xml" );

        GenerateMojo mojo = (GenerateMojo) lookupConfiguredMojo(pom, "generate");

        mojo.execute();

        assertThat(exists(outputPath), is(true));

    }

    public void testShouldGenerateFromOnlyIncludedRaml() throws Exception {
        File pom = getTestFile( "src/test/resources/includes-excludes/pom.xml" );

        GenerateMojo mojo = (GenerateMojo) lookupConfiguredMojo(pom, "generate");

        mojo.execute();

        Path outputPath = Paths.get(project.getBasedir().toString(), "target", "generated-sources", DummyGenerator.OUTPUT_FILE);
        JsonReader jsonReader = Json.createReader(newInputStream(outputPath));
        JsonObject output = jsonReader.readObject();

        Path expectedSourceDirectory = Paths.get(project.getBasedir().toString(), "src", "raml");
        Path expectedOutputDirectory = Paths.get(project.getBasedir().toString(), "target", "generated-sources");
        assertThat(output.getJsonArray("raml").getString(0), equalTo("#%RAML 0.8"));
        assertThat(output.getJsonArray("raml").size(), equalTo(1));
        assertThat(output.getJsonObject("configuration").getString("sourceDirectory"), equalTo(expectedSourceDirectory.toString()));
        assertThat(output.getJsonObject("configuration").getString("outputDirectory"), equalTo(expectedOutputDirectory.toString()));
        assertThat(output.getJsonObject("configuration").getString("basePackageName"), equalTo("uk.gov.justice.api"));

        assertThat((project.getCompileSourceRoots()), hasItem(outputPath.getParent().toAbsolutePath().toString()));
        assertThat((project.getTestCompileSourceRoots()), hasItem(outputPath.getParent().toAbsolutePath().toString()));
    }

}

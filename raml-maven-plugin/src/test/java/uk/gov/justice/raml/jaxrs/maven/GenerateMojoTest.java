package uk.gov.justice.raml.jaxrs.maven;

import org.apache.maven.plugin.MojoExecutionException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        Path outputPath = Paths.get(project.getBasedir().toString(), "target", "generated-sources", "example.json");
        JsonReader jsonReader = Json.createReader(Files.newInputStream(outputPath));
        JsonObject output = jsonReader.readObject();

        Path expectedSourceDirectory = Paths.get(project.getBasedir().toString(), "src", "raml");
        Path expectedOutputDirectory = Paths.get(project.getBasedir().toString(), "target", "generated-sources");
        assertThat(output.getString("raml"), equalTo("#%RAML 0.8" + System.getProperty("line.separator")));
        assertThat(output.getJsonObject("configuration").getString("sourceDirectory"), equalTo(expectedSourceDirectory.toString()));
        assertThat(output.getJsonObject("configuration").getString("outputDirectory"), equalTo(expectedOutputDirectory.toString()));
        assertThat(output.getJsonObject("configuration").getString("basePackageName"), equalTo("uk.gov.justice.api"));

        assertThat((project.getCompileSourceRoots()), hasItem(outputPath.getParent().toAbsolutePath().toString()));
        assertThat((project.getTestCompileSourceRoots()), hasItem(outputPath.getParent().toAbsolutePath().toString()));
    }

    //need to test shouldAddCompileSourceRootToMavenProject()


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

        Path outputPath = Paths.get(getBasedir(), "src", "test", "resources", "generate", "target", "generated-sources", "example.json");

        File pom = getTestFile( "src/test/resources/non-raml/pom.xml" );

        GenerateMojo mojo = (GenerateMojo) lookupConfiguredMojo(pom, "generate");

        mojo.execute();

        assertThat(Files.exists(outputPath), is(true));

    }

}

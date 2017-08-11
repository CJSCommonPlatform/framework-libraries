package uk.gov.justice.maven.generator.io.files.parser.generator;

import static java.nio.file.Files.exists;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;
import uk.gov.justice.maven.generator.io.files.parser.test.utils.BetterAbstractMojoTestCase;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.maven.plugin.MojoExecutionException;
import org.raml.model.Raml;

/**
 * Maven plugin harness test for the {@link GenerateMojo} class.
 */
public class GenerateMojoTest extends BetterAbstractMojoTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DummyGeneratorCaptor.getInstance().init();
    }

    public void testShouldGenerateFromValidRaml() throws Exception {
        File pom = getTestFile("src/test/resources/generate/pom.xml");

        GenerateMojo mojo = (GenerateMojo) lookupConfiguredMojo(pom, "generate");

        mojo.execute();
        List<Pair<Raml, GeneratorConfig>> capturedGeneratorArgs = DummyGeneratorCaptor.getInstance().capturedArgs();
        assertThat(capturedGeneratorArgs, hasSize(1));


        Raml raml = capturedGeneratorArgs.get(0).getLeft();
        assertThat(raml.getTitle(), equalTo("example.raml"));

        Path expectedSourceDirectory = Paths.get(project.getBasedir().toString(), "src", "raml");
        Path expectedOutputDirectory = Paths.get(project.getBasedir().toString(), "target", "generated-sources");

        GeneratorConfig config = capturedGeneratorArgs.get(0).getRight();
        assertThat(config.getSourceDirectory(), equalTo(expectedSourceDirectory));
        assertThat(config.getOutputDirectory(), equalTo(expectedOutputDirectory));
        assertThat(config.getBasePackageName(), equalTo("uk.gov.justice.api"));
        assertThat(config.getGeneratorProperties().get("property1"), equalTo("propertyValueABC"));
        assertThat(config.getGeneratorProperties().get("property2"), equalTo("propertyValueDDD"));

        assertThat((project.getCompileSourceRoots()), hasItem(expectedOutputDirectory.toString()));
        assertThat((project.getTestCompileSourceRoots()), hasItem(expectedOutputDirectory.toString()));
    }

    public void testShouldNotDeleteExistingFileInTheOutputPath() throws Exception {
        File pom = getTestFile("src/test/resources/generate/pom.xml");
        GenerateMojo mojo = (GenerateMojo) lookupConfiguredMojo(pom, "generate");

        Path existingFilePath = Paths.get(project.getBasedir().toString(), "target", "generated-sources", "existing.file");
        if (!exists(existingFilePath, LinkOption.NOFOLLOW_LINKS)) {
            Files.createFile(existingFilePath);
        }

        assertTrue("file should exist before plugin execution", exists(existingFilePath));

        mojo.execute();

        assertTrue("file should exist after plugin execution", exists(existingFilePath));
    }

    public void testShouldNotProcessInvalidRamlFile() throws Exception {

        File pom = getTestFile("src/test/resources/invalid-raml/pom.xml");

        GenerateMojo mojo = (GenerateMojo) lookupConfiguredMojo(pom, "generate");

        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException e) {

        }
    }

    public void testShouldNotTryToInstatiateGeneratorIfNoRamlFiles() throws Exception {
        //NonExisting generator defined in pom.xml

        File pom = getTestFile("src/test/resources/non-raml/pom.xml");

        GenerateMojo mojo = (GenerateMojo) lookupConfiguredMojo(pom, "generate");
        mojo.execute();
    }

    public void testShouldGenerateFromOnlyIncludedRaml() throws Exception {
        File pom = getTestFile("src/test/resources/includes-excludes/pom.xml");

        GenerateMojo mojo = (GenerateMojo) lookupConfiguredMojo(pom, "generate");

        mojo.execute();

        List<Raml> capturedRamls = DummyGeneratorCaptor.getInstance().capturedRamls();
        assertThat(capturedRamls, hasSize(1));

        Raml processedRaml = capturedRamls.get(0);
        assertThat(processedRaml.getTitle(), equalTo("my-example.raml"));

    }

    @SuppressWarnings("unchecked")
    public void testShouldIncludeRamlFilesFromTheClasspath() throws Exception {
        File pom = getTestFile("src/test/resources/includes-excludes-external/pom.xml");

        GenerateMojo mojo = (GenerateMojo) lookupConfiguredMojo(pom, "generate");

        mojo.execute();

        List<Raml> capturedRamls = DummyGeneratorCaptor.getInstance().capturedRamls();
        assertThat(capturedRamls, hasSize(2));

        assertThat(capturedRamls, containsInAnyOrder(
                hasProperty("title", equalTo("external-1.raml")),
                hasProperty("title", equalTo("external-2.raml"))));
    }

    public void testShouldSkipExecution() throws Exception {
        File pom = getTestFile("src/test/resources/skip-execution/pom.xml");
        GenerateMojo mojo = (GenerateMojo) lookupConfiguredMojo(pom, "generate");

        mojo.execute();

        List<Pair<Raml, GeneratorConfig>> capturedGeneratorArgs = DummyGeneratorCaptor.getInstance().capturedArgs();
        assertThat(capturedGeneratorArgs, hasSize(0));
    }
}

package uk.gov.justice.generation.pojo.integration.tests;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class IgnoreHardCodedClassesIT {

    private final OutputDirectories outputDirectories = new OutputDirectories();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/tests/hard-coded");
    }

    @Test
    public void shouldNotAutoGenerateClassesWhichHaveBeenCraftedByHand() throws Exception {
        final File jsonSchemaFile = new ClasspathFileResource().getFileFromClasspath("/schemas/tests/first-hard-coded-class-example.json");
        final String packageName = "uk.gov.justice.pojo.example.hard.coded.javaclass.first.testcase";

        generatorUtil
                .withIgnoredClassNames(singletonList("IgnoreMeAsImAlreadyHardCoded"))
                .generateAndCompileJavaSource(
                        jsonSchemaFile,
                        packageName,
                        outputDirectories);

        final File generatedSourceDir = new File("./target/test-generation/tests/hard-coded/uk/gov/justice/pojo/example/hard/coded/javaclass/first/testcase");

        assertThat(generatedSourceDir.exists(), is(true));

        assertThat(new File(generatedSourceDir, "FirstHardCodedClassExample.java").exists(), is(true));
        assertThat(new File(generatedSourceDir, "IgnoreMeAsImAlreadyHardCoded.java").exists(), is(false));
    }

    @Test
    public void shouldHandleTheRootObjectBeingCraftedByHand() throws Exception {
        final File jsonSchemaFile = new ClasspathFileResource().getFileFromClasspath("/schemas/tests/second-hard-coded-class-example.json");
        final String packageName = "uk.gov.justice.pojo.example.hard.coded.javaclass.second.testcase";

        generatorUtil
                .withIgnoredClassNames(singletonList("SecondHardCodedClassExample"))
                .generateAndCompileJavaSource(
                        jsonSchemaFile,
                        packageName,
                        outputDirectories);

        final File generatedSourceDir = new File("./target/test-generation/hard-coded/uk/gov/justice/pojo/example/hard/coded/javaclass/second/testcase");

        assertThat(new File(generatedSourceDir, "SecondHardCodedClassExample.java").exists(), is(false));
    }
}

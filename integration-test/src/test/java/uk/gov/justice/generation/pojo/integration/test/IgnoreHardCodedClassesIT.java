package uk.gov.justice.generation.pojo.integration.test;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class IgnoreHardCodedClassesIT {

    private final OutputDirectories outputDirectories = new OutputDirectories();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/hard-coded");
    }

    @Test
    public void shouldNotAutoGenerateClassesWhichHaveBeenCraftedByHand() throws Exception {
        final File jsonSchemaFile = new File("src/test/resources/schemas/first-hard-coded-class-example.json");
        final String packageName = "uk.gov.justice.pojo.example.hard.coded.javaclass.first.testcase";

        final List<String> ignoredClassNames = singletonList("IgnoreMeAsIAlreadyExist");

        generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                outputDirectories,
                ignoredClassNames);

        final File generatedSourceDir = new File("target/test-generation/hard-coded/uk/gov/justice/pojo/example/hard/coded/javaclass/first/testcase");

        assertThat(new File(generatedSourceDir, "FirstHardCodedClassExample.java").exists(), is(true));
        assertThat(new File(generatedSourceDir, "IgnoreMeAsImAlreadyHardCoded.java").exists(), is(false));
    }

    @Test
    public void shouldHandleTheRootObjectBeingCraftedByHand() throws Exception {
        final File jsonSchemaFile = new File("src/test/resources/schemas/second-hard-coded-class-example.json");
        final String packageName = "uk.gov.justice.pojo.example.hard.coded.javaclass.second.testcase";

        final List<String> ignoredClassNames = singletonList("SecondHardCodedClassExample");

        generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                outputDirectories,
                ignoredClassNames);

        final File generatedSourceDir = new File("target/test-generation/hard-coded/uk/gov/justice/pojo/example/hard/coded/javaclass/second/testcase");

        assertThat(new File(generatedSourceDir, "SecondHardCodedClassExample.java").exists(), is(false));
    }
}

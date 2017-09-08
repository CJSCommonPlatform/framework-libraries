package uk.gov.justice.generation.pojo.integration.test;

import static java.util.Collections.singletonList;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;

import java.io.File;
import java.util.List;

import org.everit.json.schema.Schema;
import org.junit.Before;
import org.junit.Test;

public class IgnoreHardCodedClassesIT {

    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    private File sourceOutputDirectory;
    private File classesOutputDirectory;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File("./target/test-generation/hard-coded");
        classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    @Test
    public void shouldNotAutoGenerateClassesWhichHaveBeenCraftedByHand() throws Exception {
        final File jsonSchemaFile = new File("src/test/resources/schemas/first-hard-coded-class-example.json");
        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);
        final String packageName = "uk.gov.justice.pojo.example.hard.coded.javaclass.first.testcase";

        final List<String> ignoredClassNames = singletonList("IgnoreMeAsIAlreadyExist");

        final GenerationContext generationContext = new GenerationContext(
                sourceOutputDirectory.toPath(),
                packageName,
                jsonSchemaFile.getName(),
                ignoredClassNames);

        generatorUtil.generateAndCompileJavaSource(
                generationContext,
                jsonSchemaFile,
                schema,
                classesOutputDirectory.toPath());

        final File generatedSourceDir = new File("target/test-generation/hard-coded/uk/gov/justice/pojo/example/hard/coded/javaclass/first/testcase");

        assertThat(new File(generatedSourceDir, "FirstHardCodedClassExample.java").exists(), is(true));
        assertThat(new File(generatedSourceDir, "IgnoreMeAsImAlreadyHardCoded.java").exists(), is(false));
    }

    @Test
    public void shouldHandleTheRootObjectBeingCraftedByHand() throws Exception {
        final File jsonSchemaFile = new File("src/test/resources/schemas/second-hard-coded-class-example.json");
        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);
        final String packageName = "uk.gov.justice.pojo.example.hard.coded.javaclass.second.testcase";

        final List<String> ignoredClassNames = singletonList("SecondHardCodedClassExample");

        final GenerationContext generationContext = new GenerationContext(
                sourceOutputDirectory.toPath(),
                packageName,
                jsonSchemaFile.getName(),
                ignoredClassNames);

        generatorUtil.generateAndCompileJavaSource(
                generationContext,
                jsonSchemaFile,
                schema,
                classesOutputDirectory.toPath());

        final File generatedSourceDir = new File("target/test-generation/hard-coded/uk/gov/justice/pojo/example/hard/coded/javaclass/second/testcase");

        assertThat(new File(generatedSourceDir, "SecondHardCodedClassExample.java").exists(), is(false));
    }
}

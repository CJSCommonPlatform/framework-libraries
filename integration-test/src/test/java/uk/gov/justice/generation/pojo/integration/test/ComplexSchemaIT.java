package uk.gov.justice.generation.pojo.integration.test;

import static org.apache.commons.io.FileUtils.cleanDirectory;

import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class ComplexSchemaIT {

    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    private File sourceOutputDirectory;
    private File classesOutputDirectory;


    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File("./target/test-generation/complex-schema");
        classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    @Test
    public void shouldParseAVeryComplexSchemaDocument() throws Exception {

        final File jsonSchemaFile = new File("src/test/resources/schemas/context.command.complex-data.json");
        final String packageName = "uk.gov.justice.pojo.complex.schema";

        generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                sourceOutputDirectory.toPath(),
                classesOutputDirectory.toPath());
    }

}

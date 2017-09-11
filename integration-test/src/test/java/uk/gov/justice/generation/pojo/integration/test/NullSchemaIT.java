package uk.gov.justice.generation.pojo.integration.test;

import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class NullSchemaIT {

    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    private final OutputDirectories outputDirectories = new OutputDirectories();

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/null-schema");
    }

    @Test
    public void shouldGeneratePojosFromANullSchema() throws Exception {
        final File jsonSchemaFile = new File("src/test/resources/schemas/null-schema.json");
        final String packageName = "uk.gov.justice.pojo.nullable.schema";

        generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                outputDirectories);
    }
}

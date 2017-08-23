package uk.gov.justice.generation.pojo.integration.test;

import static org.apache.commons.io.FileUtils.cleanDirectory;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.core.JsonSchemaWrapper;
import uk.gov.justice.generation.pojo.core.RootFieldNameGenerator;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.integration.utils.ClassCompiler;
import uk.gov.justice.generation.pojo.write.SourceWriter;

import java.io.File;

import org.everit.json.schema.Schema;
import org.junit.Before;
import org.junit.Test;

public class ComplexSchemaIT {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final ClassCompiler classCompiler = new ClassCompiler();

    private final JavaGeneratorFactory javaGeneratorFactory = new JavaGeneratorFactory();
    private final RootFieldNameGenerator rootFieldNameGenerator = new RootFieldNameGenerator();
    private final SchemaLoader schemaLoader = new SchemaLoader();

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
        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);
        final String fieldName = rootFieldNameGenerator.generateNameFrom(jsonSchemaFile);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor("uk.gov.justice.pojo.complex.schema");
        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(schema);

        jsonSchemaWrapper.accept(fieldName, definitionBuilderVisitor);

        javaGeneratorFactory
                .createClassGeneratorsFor(definitionBuilderVisitor.getDefinitions())
                .forEach(classGeneratable -> {
                    sourceWriter.write(classGeneratable, sourceOutputDirectory.toPath());
                    classCompiler.compile(classGeneratable, sourceOutputDirectory, classesOutputDirectory);
                });
    }
}

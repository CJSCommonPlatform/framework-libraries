package uk.gov.justice.generation.pojo.integration.test;

import static java.util.stream.Collectors.toList;

import uk.gov.justice.generation.io.files.loader.ObjectSchemaLoader;
import uk.gov.justice.generation.pojo.core.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.JsonSchemaWrapper;
import uk.gov.justice.generation.pojo.core.RootFieldNameGenerator;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.integration.utils.ClassCompiler;
import uk.gov.justice.generation.pojo.write.SourceWriter;

import java.io.File;
import java.util.List;

import org.everit.json.schema.ObjectSchema;
import org.junit.Test;

public class ComplexSchemaIT {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final ClassCompiler classCompiler = new ClassCompiler();
    private final JavaGeneratorFactory javaGeneratorFactory = new JavaGeneratorFactory();
    private final RootFieldNameGenerator rootFieldNameGenerator = new RootFieldNameGenerator();
    private final ObjectSchemaLoader objectSchemaLoader = new ObjectSchemaLoader();

    @Test
    public void shouldParseAVeryComplexSchemaDocument() throws Exception {

        final File jsonSchemaFile = new File("src/test/resources/schemas/context.command.complex-data.json");
        final ObjectSchema schema = objectSchemaLoader.loadFrom(jsonSchemaFile);
        final String fieldName = rootFieldNameGenerator.generateNameFrom(jsonSchemaFile);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor("uk.gov.justice.pojo");
        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(schema);

        jsonSchemaWrapper.accept(fieldName, definitionBuilderVisitor);

        final List<ClassGeneratable> classGeneratables = definitionBuilderVisitor.getDefinitions()
                .stream()
                .map(javaGeneratorFactory::createClassGeneratorFor)
                .collect(toList());


        final File sourceOutputDirectory = new File("./target/test-generation");
        final File classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.delete();

        final GenerationContext generationContext = new GenerationContext(sourceOutputDirectory);

        classGeneratables.forEach(classGeneratable -> {
            sourceWriter.write(classGeneratable, generationContext);
            classCompiler.compile(classGeneratable, sourceOutputDirectory, classesOutputDirectory);
        });
    }
}

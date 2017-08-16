package uk.gov.justice.generation;

import uk.gov.justice.generation.io.files.loader.ObjectSchemaLoader;
import uk.gov.justice.generation.pojo.core.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.JsonSchemaWrapper;
import uk.gov.justice.generation.pojo.core.RootFieldNameGenerator;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.write.SourceWriter;
import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.File;

import org.everit.json.schema.ObjectSchema;

public class SchemaToJavaGenerator implements Generator<File> {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final JavaGeneratorFactory javaGeneratorFactory = new JavaGeneratorFactory();
    private final RootFieldNameGenerator rootFieldNameGenerator = new RootFieldNameGenerator();
    private final ObjectSchemaLoader objectSchemaLoader = new ObjectSchemaLoader();

    @Override
    public void run(final File source, final GeneratorConfig generatorConfig) {
        final ObjectSchema schema = objectSchemaLoader.loadFrom(source);
        final String fieldName = rootFieldNameGenerator.generateNameFrom(source);

        final GenerationContext generationContext = new GenerationContext(generatorConfig.getOutputDirectory().toFile());
        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(generatorConfig.getBasePackageName());

        new JsonSchemaWrapper(schema).accept(fieldName, definitionBuilderVisitor);

        definitionBuilderVisitor.getDefinitions()
                .stream()
                .map(javaGeneratorFactory::createClassGeneratorFor)
                .forEach(classGeneratable -> sourceWriter.write(classGeneratable, generationContext));
    }
}
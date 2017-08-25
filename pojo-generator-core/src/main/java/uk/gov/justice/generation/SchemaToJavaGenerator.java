package uk.gov.justice.generation;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.ClassNameProvider;
import uk.gov.justice.generation.pojo.core.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.core.DefinitionFactory;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.JsonSchemaWrapper;
import uk.gov.justice.generation.pojo.core.NameGenerator;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.validation.FileNameValidator;
import uk.gov.justice.generation.pojo.write.JavaSourceFileProvider;
import uk.gov.justice.generation.pojo.write.NonDuplicatingSourceWriter;
import uk.gov.justice.generation.pojo.write.SourceWriter;
import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.File;

import org.everit.json.schema.Schema;

public class SchemaToJavaGenerator implements Generator<File> {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final JavaGeneratorFactory javaGeneratorFactory = new JavaGeneratorFactory();
    private final NameGenerator nameGenerator = new NameGenerator();
    private final FileNameValidator fileNameValidator = new FileNameValidator();
    private final SchemaLoader schemaLoader = new SchemaLoader();

    @Override
    public void run(final File source, final GeneratorConfig generatorConfig) {
        final NonDuplicatingSourceWriter writer = new NonDuplicatingSourceWriter(new JavaSourceFileProvider(), sourceWriter);
        final GenerationContext generationContext = new GenerationContext(generatorConfig.getOutputDirectory());

        final Schema schema = schemaLoader.loadFrom(source);
        final String fieldName = nameGenerator.rootFieldNameFrom(source);

        final DefinitionBuilderVisitor definitionBuilderVisitor = constructDefinitionBuilderVisitor(source, generatorConfig.getBasePackageName());

        new JsonSchemaWrapper(schema).accept(fieldName, definitionBuilderVisitor);

        javaGeneratorFactory
                .createClassGeneratorsFor(definitionBuilderVisitor.getDefinitions())
                .forEach(classGeneratable -> writer.write(classGeneratable, generationContext));
    }

    private DefinitionBuilderVisitor constructDefinitionBuilderVisitor(final File source, final String basePackageName) {
        if (fileNameValidator.isEventSchema(source)) {
            return new DefinitionBuilderVisitor(
                    basePackageName,
                    new DefinitionFactory(new ClassNameProvider(), nameGenerator.eventNameFrom(source)));
        }

        return new DefinitionBuilderVisitor(basePackageName, new DefinitionFactory(new ClassNameProvider()));
    }
}

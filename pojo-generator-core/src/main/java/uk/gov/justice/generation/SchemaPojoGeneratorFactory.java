package uk.gov.justice.generation;

import uk.gov.justice.generation.pojo.plugin.PluginProviderFactoryFactory;
import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorFactory;

import java.io.File;

public class SchemaPojoGeneratorFactory implements GeneratorFactory<File> {

    @Override
    public Generator<File> create() {
        return new SchemaPojoGenerator(
                new PluginProviderFactoryFactory().create(),
                new DefinitionProvider(),
                new GeneratorContextProvider(),
                new ClassNameFactoryProvider(),
                new JavaGeneratorFactoryProvider(),
                new JavaClassFileWriter());
    }
}

package uk.gov.justice.generation;

import uk.gov.justice.generation.io.files.JavaFileSimpleNameLister;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class GeneratorContextProvider {

    private final JavaFileSimpleNameLister javaFileSimpleNameLister;

    public GeneratorContextProvider(final JavaFileSimpleNameLister javaFileSimpleNameLister) {
        this.javaFileSimpleNameLister = javaFileSimpleNameLister;
    }

    public GenerationContext create(final File jsonSchemaFile, final GeneratorConfig generatorConfig) {
        final List<Path> sourcePaths = generatorConfig.getSourcePaths();
        final Path outputDirectory = generatorConfig.getOutputDirectory();
        final String basePackageName = generatorConfig.getBasePackageName();

        final List<String> hardCodedClassNames = javaFileSimpleNameLister.findSimpleNames(
                sourcePaths,
                outputDirectory,
                basePackageName);

        return new GenerationContext(
                outputDirectory,
                basePackageName,
                jsonSchemaFile.getName(),
                hardCodedClassNames);
    }
}

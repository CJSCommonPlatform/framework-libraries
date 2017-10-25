package uk.gov.justice.generation.provider;

import uk.gov.justice.generation.io.files.JavaFileSimpleNameLister;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.PackageNameParser;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.nio.file.Path;
import java.util.List;

import org.everit.json.schema.Schema;

public class GeneratorContextProvider {

    private final JavaFileSimpleNameLister javaFileSimpleNameLister;
    private final PackageNameParser packageNameParser;

    public GeneratorContextProvider(
            final JavaFileSimpleNameLister javaFileSimpleNameLister,
            final PackageNameParser packageNameParser) {
        this.javaFileSimpleNameLister = javaFileSimpleNameLister;
        this.packageNameParser = packageNameParser;
    }

    public GenerationContext create(final String schemaFileName, final Schema schema, final GeneratorConfig generatorConfig) {
        final List<Path> sourcePaths = generatorConfig.getSourcePaths();
        final Path outputDirectory = generatorConfig.getOutputDirectory();

        final String basePackageName = packageNameParser.packageNameFrom(schema.getId())
                .orElseGet(generatorConfig::getBasePackageName);

        final List<String> hardCodedClassNames = javaFileSimpleNameLister.findSimpleNames(
                sourcePaths,
                outputDirectory,
                basePackageName);

        return new GenerationContext(
                outputDirectory,
                basePackageName,
                schemaFileName,
                hardCodedClassNames);
    }
}

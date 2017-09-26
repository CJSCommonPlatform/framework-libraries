package uk.gov.justice.generation;

import uk.gov.justice.generation.io.files.JavaFileSimpleNameLister;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.File;
import java.util.List;

public class GeneratorContextProvider {

    private final JavaFileSimpleNameLister javaFileSimpleNameLister = new JavaFileSimpleNameLister();

    public GenerationContext getGenerationContext(final File jsonSchemaFile, final GeneratorConfig generatorConfig) {
        final List<String> hardCodedClassNames = javaFileSimpleNameLister.findSimpleNames(
                generatorConfig.getSourcePaths(),
                generatorConfig.getOutputDirectory(),
                generatorConfig.getBasePackageName());

        return new GenerationContext(
                generatorConfig.getOutputDirectory(),
                generatorConfig.getBasePackageName(),
                jsonSchemaFile.getName(),
                hardCodedClassNames);
    }
}

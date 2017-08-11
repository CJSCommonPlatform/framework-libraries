package uk.gov.justice.generation.pojo.write;

import static java.util.Collections.singletonList;
import static uk.gov.justice.services.generators.commons.helper.GeneratedClassWriter.writeClass;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.raml.core.GeneratorConfig;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourceWriter {

    public void write(final ClassGeneratable classGenerator, final GenerationContext generationContext) {
        final TypeSpec typeSpec = classGenerator.generate();
        final Logger logger = LoggerFactory.getLogger(getClass());
        final String packageName = classGenerator.getClassName().getPackageName();
        final String path = packageName.replace('.', '/');

        final File outputDirectory = generationContext.getSourceRootDirectory();

        //noinspection ResultOfMethodCallIgnored
        outputDirectory.mkdirs();

        final Path sourcePath = new File(outputDirectory, path).toPath();

        final GeneratorConfig configuration = new GeneratorConfig(
                outputDirectory.toPath(),
                outputDirectory.toPath(),
                packageName,
                new HashMap<>(),
                singletonList(sourcePath)
        );

        writeClass(configuration, packageName, typeSpec, logger);
    }
}

package uk.gov.justice.generation.pojo.core;

import static java.util.Collections.singletonList;
import static uk.gov.justice.services.generators.commons.helper.GeneratedClassWriter.writeClass;

import uk.gov.justice.generation.pojo.generators.ClassGenerator;
import uk.gov.justice.raml.core.GeneratorConfig;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourceWriter {

    public void write(final ClassGenerator classGenerator, final File outputDirectory) {
        final TypeSpec typeSpec = classGenerator.generate();
        final Logger logger = LoggerFactory.getLogger(getClass());
        final String packageName = classGenerator.getClassDefinition().getClassName().getPackageName();

        //noinspection ResultOfMethodCallIgnored
        outputDirectory.mkdirs();

        final Path sourcePath = new File(outputDirectory, "uk/gov/justice/pojo").toPath();

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

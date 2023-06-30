package uk.gov.justice.generation.pojo.write;

import static java.lang.String.format;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;

import java.io.IOException;
import java.nio.file.Path;

import com.squareup.javapoet.TypeSpec;

/**
 * Writes the {@link ClassGeneratable} as a *.java file to disk
 */
public class SourceWriter {

    private final JavaPoetJavaFileCreator javaPoetJavaFileCreator;

    public SourceWriter(final JavaPoetJavaFileCreator javaPoetJavaFileCreator) {
        this.javaPoetJavaFileCreator = javaPoetJavaFileCreator;
    }

    /**
     * Writes the POJO specified by the {@link ClassGeneratable} to disk as a *.java file
     *
     * @param classGenerator    The {@link ClassGeneratable} that specifies the POJO
     * @param generationContext The {@link GenerationContext}
     */
    public void write(final ClassGeneratable classGenerator, final GenerationContext generationContext) {
        final TypeSpec typeSpec = classGenerator.generate();
        final Path outputDirectory = generationContext.getOutputDirectoryPath();
        final String packageName = classGenerator.getPackageName();
        try {
            javaPoetJavaFileCreator
                    .createJavaFile(typeSpec, packageName)
                    .writeTo(outputDirectory);
        } catch (final IOException e) {
            throw new SourceCodeWriteException(format("Failed to write java file to '%s' for '%s.%s.java'", outputDirectory, packageName, typeSpec.name), e);
        }
    }
}

package uk.gov.justice.generation.pojo.write;

import static java.lang.String.format;

import uk.gov.justice.generation.pojo.generators.ClassGeneratable;

import java.io.IOException;
import java.nio.file.Path;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

public class SourceWriter {

    public void write(final ClassGeneratable classGenerator, final Path outputDirectory) {
        final TypeSpec typeSpec = classGenerator.generate();
        final String packageName = classGenerator.getClassName().getPackageName();

        writeClass(outputDirectory, packageName, typeSpec);
    }

    private void writeClass(final Path outputDirectory, final String packageName, final TypeSpec typeSpec) {
        try {
            JavaFile.builder(packageName, typeSpec)
                    .build()
                    .writeTo(outputDirectory);
        } catch (IOException e) {
            throw new SourceCodeWriteException(format("Failed to write java file to '%s' for '%s.%s.java'", outputDirectory, packageName, typeSpec.name), e);
        }
    }
}

package uk.gov.justice.generation.pojo.write;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;

import java.util.List;

public class JavaClassFileWriter {

    private final NonDuplicatingSourceWriter writer;

    public JavaClassFileWriter(final NonDuplicatingSourceWriter writer) {
        this.writer = writer;
    }

    public void writeJavaClassesToFile(final GenerationContext generationContext, final List<ClassGeneratable> classGenerators) {
        classGenerators.forEach(classGeneratable -> writer.write(classGeneratable, generationContext));
    }
}

package uk.gov.justice.generation;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.write.JavaSourceFileProvider;
import uk.gov.justice.generation.pojo.write.NonDuplicatingSourceWriter;
import uk.gov.justice.generation.pojo.write.SourceWriter;

import java.util.List;

public class JavaClassFileWriter {

    private final SourceWriter sourceWriter = new SourceWriter();

    public void writeJavaClassesToFile(final GenerationContext generationContext, final List<ClassGeneratable> classGenerators) {
        final NonDuplicatingSourceWriter writer = new NonDuplicatingSourceWriter(new JavaSourceFileProvider(), sourceWriter);
        classGenerators.forEach(classGeneratable -> writer.write(classGeneratable, generationContext));
    }
}

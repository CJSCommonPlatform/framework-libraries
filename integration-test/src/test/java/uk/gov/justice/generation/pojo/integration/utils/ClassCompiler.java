package uk.gov.justice.generation.pojo.integration.utils;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.services.test.utils.core.compiler.JavaCompilerUtil;

import java.io.File;

public class ClassCompiler {

    public Class<?> compile(
            final ClassGeneratable classGenerator,
            final GenerationContext generationContext,
            final File classesOutputDirectory) {

        final JavaCompilerUtil compiler = new JavaCompilerUtil(generationContext.getOutputDirectoryPath().toFile(), classesOutputDirectory);
        return compiler.compiledClassOf(generationContext.getPackageName(), classGenerator.getClassName());
    }
}

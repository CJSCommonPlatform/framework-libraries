package uk.gov.justice.generation.pojo.integration.utils;

import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.services.generators.test.utils.compiler.JavaCompilerUtil;

import java.io.File;

public class ClassCompiler {

    public Class<?> compile(
            final ClassGeneratable classGenerator,
            final File sourceDirectory,
            final File classesOutputDirectory) {

        final ClassName className = classGenerator.getClassName();

        final JavaCompilerUtil compiler = new JavaCompilerUtil(sourceDirectory, classesOutputDirectory);
        return compiler.compiledClassOf(className.getPackageName(), className.getSimpleName());
    }
}

package uk.gov.justice.services.test.utils.core.compiler;

import static uk.gov.justice.services.test.utils.core.compiler.JavaCompilerUtility.javaCompilerUtil;

import java.io.File;
import java.util.Set;

/**
 * Compiles and loads classes and interfaces from the specified folders
 *
 * @deprecated Use {@link JavaCompilerUtility}
 */
@Deprecated
public class JavaCompilerUtil {

    private final File codegenOutputDir;
    private final File compilationOutputDir;

    private final JavaCompilerUtility javaCompilerUtility = javaCompilerUtil();

    public JavaCompilerUtil(final File codegenOutputDir, final File compilationOutputDir) {
        this.codegenOutputDir = codegenOutputDir;
        this.compilationOutputDir = compilationOutputDir;
    }

    /**
     * Compiles and loads a single class
     *
     * @param basePackage - the base package of the class to be compiled
     * @return the Class
     * @throws IllegalStateException - if more or less than one classes found
     */
    public Class<?> compiledClassOf(final String basePackage) {
        return javaCompilerUtility.compiledClassOf(codegenOutputDir, compilationOutputDir, basePackage);
    }

    /**
     * Compiles then finds a single class.
     *
     * @param basePackage              the base package
     * @param additionalFilterElements the additional filter elements
     * @return the class
     * @throws IllegalStateException - if more or less than one classes found
     */
    public Class<?> compiledClassOf(final String basePackage, final String... additionalFilterElements) {
        return javaCompilerUtility.compiledClassOf(codegenOutputDir, compilationOutputDir, basePackage, additionalFilterElements);
    }

    /**
     * Compiles then finds a single interface class.
     *
     * @param basePackage              the base package
     * @param additionalFilterElements the additional filter elements
     * @return the class
     * @throws IllegalStateException - if more or less than one classes found
     */
    public Class<?> compiledInterfaceClassOf(final String basePackage, final String... additionalFilterElements) {
        return javaCompilerUtility.compiledInterfaceClassOf(codegenOutputDir, compilationOutputDir, basePackage, additionalFilterElements);
    }

    public Class<?> classOf(final Set<Class<?>> classes, final String basePackage, final String... additionalFilterElements) {
        return javaCompilerUtility.classOf(classes, basePackage, additionalFilterElements);
    }

    /**
     * Compiles and loads a single interface
     *
     * @param basePackageName - the base package of the interface to be compiled
     * @return the Class
     * @throws IllegalStateException - if more or less than one interfaces found
     */
    public Class<?> compiledInterfaceOf(final String basePackageName) {
        return javaCompilerUtility.compiledInterfaceOf(codegenOutputDir, compilationOutputDir, basePackageName);
    }

    /**
     * compiles and loads specified classes
     *
     * @param basePackage - the base package of the classes to be compiled
     * @return the set of classes
     */
    public Set<Class<?>> compiledClassesOf(final String basePackage) {
        return javaCompilerUtility.compiledClassesOf(codegenOutputDir, compilationOutputDir, basePackage);
    }

    /**
     * compiles and loads specified interfaces
     *
     * @param basePackage - the base package of the interfaces to be compiled
     * @return the set of classes
     */
    public Set<Class<?>> compiledInterfacesOf(final String basePackage) {
        return javaCompilerUtility.compiledInterfacesOf(codegenOutputDir, compilationOutputDir, basePackage);
    }
}
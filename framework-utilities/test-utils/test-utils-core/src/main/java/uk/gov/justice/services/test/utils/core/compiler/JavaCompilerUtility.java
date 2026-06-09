package uk.gov.justice.services.test.utils.core.compiler;

import static java.lang.String.join;
import static java.text.MessageFormat.format;
import static java.util.stream.Collectors.toSet;
import static org.reflections.scanners.Scanners.SubTypes;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

public class JavaCompilerUtility {

    private static final String CLASS_EXCEPTION_MESSAGE_FORMAT = "Expected to find single class but found {0}";
    private static final String INTERFACE_EXCEPTION_MESSAGE_FORMAT = "Expected to find single interface but found {0}";

    private final Compiler compiler;

    public static JavaCompilerUtility javaCompilerUtil() {
        return new JavaCompilerUtility(new Compiler());
    }

    public JavaCompilerUtility(final Compiler compiler) {
        this.compiler = compiler;
    }

    /**
     * Compiles and loads a single class
     *
     * @param codegenOutputDir     the code generation output directory
     * @param compilationOutputDir the compilation output directory
     * @param basePackage          the base package of the class to be compiled
     * @return the Class
     * @throws IllegalStateException - if more or less than one classes found
     */
    public Class<?> compiledClassOf(final File codegenOutputDir,
                                    final File compilationOutputDir,
                                    final String basePackage) {
        final Set<Class<?>> resourceClasses = compiledClassesOf(
                codegenOutputDir,
                compilationOutputDir,
                basePackage);

        if (resourceClasses.size() != 1) {
            throw new IllegalStateException(format(CLASS_EXCEPTION_MESSAGE_FORMAT, resourceClasses));
        }

        return resourceClasses.iterator().next();
    }

    /**
     * Compiles then finds a single class.
     *
     * @param codegenOutputDir         the code generation output directory
     * @param compilationOutputDir     the compilation output directory
     * @param basePackage              the base package
     * @param additionalFilterElements the additional filter elements
     * @return the class
     * @throws IllegalStateException - if more or less than one classes found
     */
    public Class<?> compiledClassOf(final File codegenOutputDir,
                                    final File compilationOutputDir,
                                    final String basePackage,
                                    final String... additionalFilterElements) {
        return compiledClassOf(
                codegenOutputDir,
                compilationOutputDir,
                basePackage,
                aClass -> !aClass.isInterface(),
                additionalFilterElements);
    }

    /**
     * Compiles then finds a single interface class.
     *
     * @param codegenOutputDir         the code generation output directory
     * @param compilationOutputDir     the compilation output directory
     * @param basePackage              the base package
     * @param additionalFilterElements the additional filter elements
     * @return the class
     * @throws IllegalStateException - if more or less than one classes found
     */
    public Class<?> compiledInterfaceClassOf(final File codegenOutputDir,
                                             final File compilationOutputDir,
                                             final String basePackage,
                                             final String... additionalFilterElements) {
        return compiledClassOf(
                codegenOutputDir,
                compilationOutputDir,
                basePackage,
                Class::isInterface,
                additionalFilterElements);
    }

    public Class<?> classOf(final Set<Class<?>> classes,
                            final String basePackage,
                            final String... additionalFilterElements) {
        final Set<Class<?>> resourceClasses = classesMatching(
                classes,
                aClass -> !aClass.isInterface() && aClass.getName().equals(join(".", basePackage, join(".", additionalFilterElements))));

        if (resourceClasses.size() != 1) {
            throw new IllegalStateException(format(CLASS_EXCEPTION_MESSAGE_FORMAT, resourceClasses));
        }
        return resourceClasses.iterator().next();
    }

    /**
     * Compiles and loads a single interface
     *
     * @param codegenOutputDir     the code generation output directory
     * @param compilationOutputDir the compilation output directory
     * @param basePackageName      the base package of the interface to be compiled
     * @return the Class
     * @throws IllegalStateException - if more or less than one interfaces found
     */
    public Class<?> compiledInterfaceOf(final File codegenOutputDir,
                                        final File compilationOutputDir,
                                        final String basePackageName) {
        final Set<Class<?>> resourceInterfaces = compiledInterfacesOf(
                codegenOutputDir,
                compilationOutputDir,
                basePackageName);

        if (resourceInterfaces.size() != 1) {
            throw new IllegalStateException(
                    format(INTERFACE_EXCEPTION_MESSAGE_FORMAT, resourceInterfaces));

        }

        return resourceInterfaces.iterator().next();
    }

    /**
     * compiles and loads specified classes
     *
     * @param codegenOutputDir     the code generation output directory
     * @param compilationOutputDir the compilation output directory
     * @param basePackage          the base package of the classes to be compiled
     * @return the set of classes
     */
    public Set<Class<?>> compiledClassesOf(final File codegenOutputDir,
                                           final File compilationOutputDir,
                                           final String basePackage) {
        return compiledClassesAndInterfaces(
                codegenOutputDir,
                compilationOutputDir,
                aClass -> !aClass.isInterface(),
                basePackage);
    }

    /**
     * compiles and loads specified interfaces
     *
     * @param codegenOutputDir     the code generation output directory
     * @param compilationOutputDir the compilation output directory
     * @param basePackage          the base package of the interfaces to be compiled
     * @return the set of classes
     */
    public Set<Class<?>> compiledInterfacesOf(final File codegenOutputDir,
                                              final File compilationOutputDir,
                                              final String basePackage) {
        return compiledClassesAndInterfaces(
                codegenOutputDir,
                compilationOutputDir,
                Class::isInterface,
                basePackage);
    }

    private Class<?> compiledClassOf(final File codegenOutputDir,
                                     final File compilationOutputDir,
                                     final String basePackage,
                                     final Predicate<Class<?>> predicate,
                                     final String... additionalFilterElements) {

        final Set<Class<?>> resourceClasses = compiledClassesAndInterfaces(
                codegenOutputDir,
                compilationOutputDir,
                aClass -> predicate.test(aClass) && aClass.getName().equals(join(".", basePackage, join(".", additionalFilterElements))),
                basePackage);

        if (resourceClasses.size() != 1) {
            throw new IllegalStateException(format(CLASS_EXCEPTION_MESSAGE_FORMAT, resourceClasses));
        }

        return resourceClasses.iterator().next();
    }

    private Set<Class<?>> compiledClassesAndInterfaces(final File codegenOutputDir,
                                                       final File compilationOutputDir,
                                                       final Predicate<? super Class<?>> predicate,
                                                       final String basePackage) {
        return classesMatching(compile(codegenOutputDir, compilationOutputDir, basePackage), predicate);
    }

    private Set<Class<?>> classesMatching(final Set<Class<?>> classes,
                                          final Predicate<? super Class<?>> predicate) {
        return classes.stream().filter(predicate).collect(toSet());
    }

    private Set<Class<?>> compile(final File codegenOutputDir,
                                  final File compilationOutputDir,
                                  final String basePackage) {
        compiler.compile(codegenOutputDir, compilationOutputDir);
        return loadClasses(compilationOutputDir, basePackage);
    }

    private Set<Class<?>> loadClasses(final File compilationOutputDir, final String basePackage) {
        try (URLClassLoader resourceClassLoader = new URLClassLoader(new URL[]{compilationOutputDir.toURI().toURL()})) {
            final Reflections reflections = new Reflections(
                    new ConfigurationBuilder()
                            .forPackage(basePackage, resourceClassLoader)
                            .addScanners(SubTypes.filterResultsBy(s -> true)));
            return getClassNames(reflections).stream()
                    .map(name -> loadClass(name, resourceClassLoader))
                    .collect(toSet());
        } catch (IOException ex) {
            throw new CompilationException("Error creating class loader", ex);
        }
    }

    private Set<String> getClassNames(final Reflections reflections) {
        final Map<String, Set<String>> types = reflections.getStore().get(SubTypes.index());
        return Stream.concat(types.values().stream().flatMap(Set::stream), types.keySet().stream()).collect(toSet());
    }

    private static Class<?> loadClass(final String name, final ClassLoader classLoader) {
        try {
            return classLoader.loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new CompilationException("Class not found: " + name, e);
        }
    }
}

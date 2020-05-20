package uk.gov.justice.services.test.utils.core.compiler;

import static java.text.MessageFormat.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Compiler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Compiler.class);

    private final JavaCompiler javaCompiler;

    public Compiler() {
        this.javaCompiler = ToolProvider.getSystemJavaCompiler();
    }

    public void compile(final File codegenOutputDir, final File compilationOutputDir) {

        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        try (final StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(diagnostics, Locale.getDefault(), null)) {

            final List<JavaFileObject> javaObjects = scanForJavaObjects(codegenOutputDir, fileManager);

            if (javaObjects.isEmpty()) {
                throw new CompilationException(
                        format("There are no source files to compile in {0}", codegenOutputDir.getAbsolutePath()));
            }

            final String[] compileOptions = new String[]{"-d", compilationOutputDir.getAbsolutePath()};
            final Iterable<String> compilationOptions = Arrays.asList(compileOptions);

            final JavaCompiler.CompilationTask compilerTask = javaCompiler.getTask(null, fileManager, diagnostics, compilationOptions, null,
                    javaObjects);

            if (!compilerTask.call()) {
                for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                    LOGGER.error("Error on line {} in {}", diagnostic.getLineNumber(), diagnostic);
                }
                throw new CompilationException("Could not compile project");
            }

        } catch (final IOException e) {
            throw new CompilationException("Could not compile project", e);
        }
    }

    private List<JavaFileObject> scanForJavaObjects(final File dir, final StandardJavaFileManager fileManager) {
        return scanRecursivelyForJavaObjects(dir, fileManager).collect(toList());
    }

    private Stream<JavaFileObject> scanRecursivelyForJavaObjects(final File dir, final StandardJavaFileManager fileManager) {

        final File[] fileList = dir.listFiles();

        if (null == fileList) {
            return Stream.empty();
        }

        return stream(fileList)
                .flatMap(file -> processFileForJavaObjects(fileManager, file));

    }

    private Stream<? extends JavaFileObject> processFileForJavaObjects(final StandardJavaFileManager fileManager, final File file) {
        if (file.isDirectory()) {
            return scanRecursivelyForJavaObjects(file, fileManager);
        } else if (file.isFile() && file.getName().toLowerCase().endsWith(".java")) {

            try {
                FileUtils.readFileToString(file);
            } catch (final IOException e) {
                LOGGER.warn("Could not read file : {0}", file.getAbsolutePath(), e);
            }

            return readJavaObjects(file, fileManager);
        }

        return Stream.empty();
    }

    private Stream<JavaFileObject> readJavaObjects(final File file, final StandardJavaFileManager fileManager) {
        final Iterator<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjects(file).iterator();

        if (!javaFileObjects.hasNext()) {
            throw new CompilationException(format("Could not load {0} java file object", file.getAbsolutePath()));
        }

        final Stream.Builder<JavaFileObject> builder = Stream.builder();

        javaFileObjects.forEachRemaining(o -> builder.accept(o));

        return builder.build();
    }
}

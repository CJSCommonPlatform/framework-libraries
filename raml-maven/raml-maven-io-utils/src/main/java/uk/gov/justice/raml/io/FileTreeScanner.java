package uk.gov.justice.raml.io;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.base.Predicate;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ResourceList;
import io.github.classgraph.ScanResult;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 * Utility class for searching directories.
 */
public class FileTreeScanner {

    private static final String CLASSPATH = "CLASSPATH";
    private static final String RAML_PATTERN = "**/*.raml";

    /**
     * Finding all files within a directory that fulfil a set of include and exclude patterns, using standard
     * Ant patterns - {@see http://ant.apache.org/manual/dirtasks.html#patterns}.
     *
     * @param baseDir  the path to search under
     * @param includes the path patterns to include
     * @param excludes the path patterns to exclude
     * @return a list of paths to matching files under the specified directory
     */
    public Collection<Path> find(final Path baseDir, final String[] includes, final String[] excludes) throws IOException {
        if (!shouldSearchOnClasspath(baseDir) && !baseDir.toFile().exists()) {
            return emptyList();
        }

        return getResources(baseDir, includes, excludes)
                .stream()
                .map(Paths::get)
                .collect(toList());
    }

    private Set<String> getResources(final Path baseDir, final String[] includes, final String[] excludes) throws MalformedURLException {

        if(shouldSearchOnClasspath(baseDir)) {
           return getFromClasspath(includes, excludes);
        }

        return getFromPath(baseDir, includes, excludes);
    }

    private Set<String> getFromPath(final Path baseDir, final String[] includes, final String[] excludes) throws MalformedURLException {
        final Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .filterInputsBy(filterOf(includes, excludes))
                        .setUrls(singletonList(baseDir.toUri().toURL()))
                        .setScanners(new ResourcesScanner()));
        return reflections.getResources(Pattern.compile(".*"));
    }

    private Set<String> getFromClasspath(final String[] includes, final String[] excludes) {
        try (final ScanResult scanResult =
                     new ClassGraph()
                             .enableAllInfo()
                             .scan()) {

            final ResourceList ramlFiles = scanResult
                    .getAllResources()
                    .filter(resource -> filterOf(includes, excludes).apply(resource.getPath()));

            return new HashSet<>(ramlFiles.getPaths());
        }
    }

    private boolean shouldSearchOnClasspath(final Path baseDir) {
        return baseDir == null || baseDir.toString().contains(CLASSPATH);
    }

    private Predicate<String> filterOf(final String[] includes, final String[] excludes) {
        List<AntPathMatcher> includesMatcher = stream(includes).map(i -> new AntPathMatcher(i)).collect(toList());
        Predicate<String> includesFilter = notEmpty(includes)
                ? or(includesMatcher)
                : new AntPathMatcher(RAML_PATTERN);

        List<Predicate<String>> excludesMatcher = stream(excludes).map(i -> not(new AntPathMatcher(i))).collect(toList());
        Predicate<String> excludesFilter = and(excludesMatcher);

        return and(includesFilter, excludesFilter);
    }

    private boolean notEmpty(final String[] includes) {
        return includes != null && includes.length > 0;
    }

}

package uk.gov.justice.maven.generator.io.files.parser.generator;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.unmodifiableIterable;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.Thread.currentThread;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;

public class ProjectDependencyLoader {

    private final MavenProject project;

    public ProjectDependencyLoader(final MavenProject project) {
        this.project = project;
    }

    public void loadProjectDependencies() throws DependencyResolutionRequiredException, MalformedURLException {

        final Set<URL> dependenciesUrl = newHashSet();

        final Iterable<String> combinedDependencies = unmodifiableIterable(
                concat(project.getRuntimeClasspathElements(), project.getCompileClasspathElements()));

        final List<String> dependencies =  newArrayList(combinedDependencies);

        for (final String dependency : dependencies) {
            final URL dependencyUrl = new File(dependency).toURI().toURL();
            if (dependencyUrl.toString().endsWith(".jar")) {
                dependenciesUrl.add(dependencyUrl);
            }
        }

        final ClassLoader contextClassLoader = URLClassLoader.newInstance(
                dependenciesUrl.toArray(new URL[0]),
                currentThread().getContextClassLoader());

        currentThread().setContextClassLoader(contextClassLoader);
    }
}

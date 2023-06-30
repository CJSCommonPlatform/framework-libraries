package uk.gov.justice.maven.generator.io.files.parser.generator;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Thread.currentThread;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.List;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProjectDependencyLoaderTest {

    @Mock
    private MavenProject project;

    @InjectMocks
    private ProjectDependencyLoader projectDependencyLoader;

    @Test
    public void shouldLoadProjectDependenciesSuccessfully() throws DependencyResolutionRequiredException, MalformedURLException {

        final List<String> compileTimeClasspathElements = newArrayList("example.jar");

        when(project.getRuntimeClasspathElements()).thenReturn(newArrayList());
        when(project.getCompileClasspathElements()).thenReturn(compileTimeClasspathElements);

        projectDependencyLoader.loadProjectDependencies();

        final URLClassLoader parent = (URLClassLoader)currentThread().getContextClassLoader();

        assertThat(parent.getURLs().length, is(1));
        assertThat(parent.getURLs()[0].toString(), endsWith("generator-maven-plugin/generator-plugin/example.jar"));
    }

    @Test
    public void shouldNotFailedWhenProjectDependenciesJarNotFound() throws DependencyResolutionRequiredException, MalformedURLException {

        final List<String> compileTimeClasspathElements = newArrayList("fileNotFoundInClassPath");

        when(project.getRuntimeClasspathElements()).thenReturn(newArrayList());
        when(project.getCompileClasspathElements()).thenReturn(compileTimeClasspathElements);

        projectDependencyLoader.loadProjectDependencies();

        final URLClassLoader parent = (URLClassLoader)currentThread().getContextClassLoader();

        assertThat(parent.getURLs().length, is(0));
    }

}
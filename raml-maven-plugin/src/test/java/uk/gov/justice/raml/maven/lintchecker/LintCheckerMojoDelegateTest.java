package uk.gov.justice.raml.maven.lintchecker;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.raml.model.Raml;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class LintCheckerMojoDelegateTest {

    @Mock
    private RamlProvider ramlProvider;

    @Mock
    private PathsProvider pathsProvider;

    @InjectMocks
    private LintCheckerMojoDelegate lintCheckerMojoDelegate;

    @Test
    public void shouldExecuteAllRulesOnAllRamls() throws Exception {

        final LintCheckRule lintCheckRule_1 = mock(LintCheckRule.class);
        final LintCheckRule lintCheckRule_2 = mock(LintCheckRule.class);
        final Raml raml_1 = mock(Raml.class);
        final Raml raml_2 = mock(Raml.class);

        final Collection<Raml> ramls = asList(raml_1, raml_2);
        final Collection<Path> paths = singletonList(mock(Path.class));

        final File sourceDirectory = new File(("."));
        final List<LintCheckRule> rules = asList(lintCheckRule_1, lintCheckRule_2);
        final List<String> excludes = singletonList("exclude_1");
        final List<String> includes = singletonList("include_1");

        when(pathsProvider.getPaths(sourceDirectory, includes, excludes)).thenReturn(paths);
        when(ramlProvider.getRamls(sourceDirectory, paths)).thenReturn(ramls);

        lintCheckerMojoDelegate.execute(sourceDirectory, rules, includes, excludes);

        verify(lintCheckRule_1).execute(raml_1);
        verify(lintCheckRule_1).execute(raml_2);
        verify(lintCheckRule_2).execute(raml_1);
        verify(lintCheckRule_2).execute(raml_2);
    }

    @Test
    public void shouldThrowMojoExecutionExceptionAIfExecutingARuleFails() throws Exception {

        final LintCheckerException lintCheckerException = new LintCheckerException("Ooops");

        final LintCheckRule lintCheckRule_1 = mock(LintCheckRule.class);
        final LintCheckRule lintCheckRule_2 = mock(LintCheckRule.class);
        final Raml raml_1 = mock(Raml.class);
        final Raml raml_2 = mock(Raml.class);

        final Collection<Raml> ramls = asList(raml_1, raml_2);
        final Collection<Path> paths = singletonList(mock(Path.class));

        final File sourceDirectory = new File(("."));
        final List<LintCheckRule> rules = asList(lintCheckRule_1, lintCheckRule_2);
        final List<String> excludes = singletonList("exclude_1");
        final List<String> includes = singletonList("include_1");

        when(pathsProvider.getPaths(sourceDirectory, includes, excludes)).thenReturn(paths);
        when(ramlProvider.getRamls(sourceDirectory, paths)).thenReturn(ramls);

        doThrow(lintCheckerException).when(lintCheckRule_1).execute(raml_2);

        try {
            lintCheckerMojoDelegate.execute(sourceDirectory, rules, includes, excludes);
            fail();
        } catch (final MojoExecutionException expected) {
            assertThat(expected.getCause(), is(lintCheckerException));
            assertThat(expected.getMessage(), is("Lint checker rule failed for rule " + lintCheckRule_1.getClass().getSimpleName()));
        }

        verify(lintCheckRule_1).execute(raml_1);
    }
}

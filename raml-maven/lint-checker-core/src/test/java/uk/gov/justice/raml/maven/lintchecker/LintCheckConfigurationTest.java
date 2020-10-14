package uk.gov.justice.raml.maven.lintchecker;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LintCheckConfigurationTest {

    @Mock
    private MavenProject mavenProject;

    @Mock
    private Log log;

    @Test
    public void shouldCreateConfiguration() {

        final LintCheckConfiguration lintCheckConfiguration = new LintCheckConfiguration(mavenProject, log);

        assertThat(lintCheckConfiguration.getLog(), is(log));
        assertThat(lintCheckConfiguration.getMavenProject(), is(mavenProject));


    }

}
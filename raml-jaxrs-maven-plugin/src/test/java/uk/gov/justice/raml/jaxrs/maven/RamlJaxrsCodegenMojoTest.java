package uk.gov.justice.raml.jaxrs.maven;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import uk.gov.justice.raml.jaxrs.core.Configuration;
import uk.gov.justice.raml.jaxrs.core.Generator;
import uk.gov.justice.raml.jaxrs.maven.RamlJaxrsCodegenMojo;

public class RamlJaxrsCodegenMojoTest {
    private RamlJaxrsCodegenMojo pluginMojo;
    private MavenProject project;

    @Rule
    public TemporaryFolder outputFolder = new TemporaryFolder();

    @Rule
    public TemporaryFolder sourceDirectory = new TemporaryFolder();

    @Mock
    private Generator generator;

    @Before
    public void before() throws IOException {
        initMocks(this);

        pluginMojo = new RamlJaxrsCodegenMojo(generator);
        pluginMojo.setSourceDirectory(sourceDirectory.getRoot());
        pluginMojo.setOutputDirectory(outputFolder.getRoot());
        project = new MavenProject();
        pluginMojo.setProject(project);

    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldThrowExceptionIfSourceDirectoryNotSet() throws Exception {
        pluginMojo.setSourceDirectory(null);
        thrown.expect(MojoExecutionException.class);
        thrown.expectMessage("SourceDirectory must be provided");
        pluginMojo.execute();
    }

    @Test
    public void shouldPassConfigurationToTheGenerator() throws Exception {
        File outputDirectory = new File("oDir123");
        FileUtils.write(sourceDirectory.newFile("file1.raml"), "#%RAML 0.8");
        String basePackageName = "base.pckge.abc";

        pluginMojo.setOutputDirectory(outputDirectory);
        pluginMojo.setSourceDirectory(sourceDirectory.getRoot());
        pluginMojo.setBasePackageName(basePackageName);
        pluginMojo.execute();

        ArgumentCaptor<Configuration> configCaptor = ArgumentCaptor.forClass(Configuration.class);
        verify(generator).run(any(String.class), configCaptor.capture());
        Configuration configuration = configCaptor.getValue();
        assertThat(configuration.getBasePackageName(), is(basePackageName));
        assertThat(configuration.getSourceDirectory(), is(sourceDirectory.getRoot()));
        assertThat(configuration.getOutputDirectory(), is(outputDirectory));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldAddCompileSourceRootToMavenProject() throws Exception {
        String outputDirPath = "oDir1456";
        pluginMojo.setOutputDirectory(new File(outputDirPath));
        pluginMojo.execute();
        assertThat(((List<String>) project.getCompileSourceRoots()), hasItem(outputDirPath));

    }

    @Test
    public void shouldPassRAMLFilesToGenerator() throws Exception {

        File ramlFile = sourceDirectory.newFile("file1.raml");
        String ramlString1 = "#%RAML 0.8 abcd";
        FileUtils.write(ramlFile, ramlString1);
        File ramlFile2 = sourceDirectory.newFile("file2.raml");
        String ramlString2 = "#%RAML 0.8 efdh";
        FileUtils.write(ramlFile2, ramlString2);
        pluginMojo.execute();

        verify(generator).run(eq(ramlString1), any(Configuration.class));
        verify(generator).run(eq(ramlString2), any(Configuration.class));
    }

    @Test
    public void shouldNotProcessEmptyFile() throws Exception {
        sourceDirectory.newFile("file3.raml");
        pluginMojo.execute();
        verifyZeroInteractions(generator);
    }

    @Test
    public void shouldNotProcessFileWithNoRamlHeader() throws Exception {

        File ramlFile = sourceDirectory.newFile("file1.raml");
        FileUtils.write(ramlFile, "abcde");
        pluginMojo.execute();
        verifyZeroInteractions(generator);

    }

    @Test
    public void shouldNotProcessNonRAMLFile() throws Exception {

        File ramlFile = sourceDirectory.newFile("file1.notraml");
        FileUtils.write(ramlFile, "#%RAML 0.8");

        pluginMojo.execute();

        verifyZeroInteractions(generator);
    }

}

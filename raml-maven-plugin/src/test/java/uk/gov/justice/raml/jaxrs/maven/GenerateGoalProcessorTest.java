package uk.gov.justice.raml.jaxrs.maven;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.raml.emitter.RamlEmitter;
import org.raml.model.Raml;
import uk.gov.justice.raml.core.Generator;
import uk.gov.justice.raml.core.GeneratorConfig;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by jcooke on 10/03/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class GenerateGoalProcessorTest {

    @Mock
    private Generator generator;

    @Mock
    private GeneratorFactory generatorFactory;

    @Mock
    private GenerateGoalConfig config;

    @Rule
    public TemporaryFolder sourceDirectory = new TemporaryFolder();

    @InjectMocks
    private GenerateGoalProcessor generateGoalProcessor;


    @Before
    public void setup() {
        String generatorName = "mock.generator";

        when(generatorFactory.create(generatorName)).thenReturn(generator);
        when(config.getGeneratorName()).thenReturn(generatorName);
        when(config.getSourceDirectory()).thenReturn(sourceDirectory.getRoot().toPath());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCallGeneratorWithRamlFilesAndConfig() throws Exception {

        File ramlFile = sourceDirectory.newFile("file1.raml");
        String ramlString1 = "#%RAML 0.8\nbaseUri: \"http://a:8080/\"\n";
        FileUtils.write(ramlFile, ramlString1);
        File ramlFile2 = sourceDirectory.newFile("file2.raml");
        String ramlString2 = "#%RAML 0.8\nbaseUri: \"http://b:8080/\"\n";
        FileUtils.write(ramlFile2, ramlString2);

        generateGoalProcessor.generate(config);

        ArgumentCaptor<Raml> ramlCaptor = ArgumentCaptor.forClass(Raml.class);
        verify(generator, times(2)).run(ramlCaptor.capture(), any(GeneratorConfig.class));

        List<Raml> ramls = ramlCaptor.getAllValues();

        RamlEmitter emitter = new RamlEmitter();
        assertThat(
                ramls.stream()
                        .map(emitter::dump)
                        .collect(Collectors.toSet()),
                containsInAnyOrder(
                        equalTo(ramlString1),
                        equalTo(ramlString2)
                )
        );
    }

    @Test
    public void shouldCallGeneratorWithEmptyRamlForEmptyFile() throws Exception {
        sourceDirectory.newFile("file3.raml");

        generateGoalProcessor.generate(config);

        ArgumentCaptor<Raml> ramlCaptor = ArgumentCaptor.forClass(Raml.class);
        verify(generator).run(ramlCaptor.capture(), any(GeneratorConfig.class));

        Raml raml = ramlCaptor.getValue();

        RamlEmitter emitter = new RamlEmitter();
        assertThat(emitter.dump(raml), equalTo("#%RAML 0.8\n"));
    }
}
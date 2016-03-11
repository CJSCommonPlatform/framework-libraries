package uk.gov.justice.raml.jaxrs.maven;

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
import uk.gov.justice.raml.io.FileTreeScanner;
import uk.gov.justice.raml.io.FileTreeScannerFactory;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.apache.commons.io.FileUtils.write;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link GenerateGoalProcessor} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class GenerateGoalProcessorTest {

    private final String[] includes = {"**/*.raml"};
    private final String[] excludes = {};

    @Mock
    private Generator generator;

    @Mock
    private GeneratorFactory generatorFactory;

    @Mock
    private FileTreeScannerFactory scannerFactory;

    @Mock
    private GenerateGoalConfig config;

    @Mock
    private FileTreeScanner scanner;

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
        when(config.getIncludes()).thenReturn(asList(includes));
        when(config.getExcludes()).thenReturn(asList(excludes));
        when(scannerFactory.create()).thenReturn(scanner);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCallGeneratorWithRamlFilesAndConfig() throws Exception {

        File ramlFile = sourceDirectory.newFile("file1.raml");
        String ramlString1 = "#%RAML 0.8\nbaseUri: \"http://a:8080/\"\n";
        write(ramlFile, ramlString1);
        File ramlFile2 = sourceDirectory.newFile("file2.raml");
        String ramlString2 = "#%RAML 0.8\nbaseUri: \"http://b:8080/\"\n";
        write(ramlFile2, ramlString2);
        when(scanner.find(sourceDirectory.getRoot().toPath(), includes, excludes))
                .thenReturn(asList(ramlFile.toPath(), ramlFile2.toPath()));

        generateGoalProcessor.generate(config);

        ArgumentCaptor<Raml> ramlCaptor = forClass(Raml.class);
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
        File ramlFile = sourceDirectory.newFile("file3.raml");

        when(scanner.find(sourceDirectory.getRoot().toPath(), includes, excludes))
                .thenReturn(singletonList(ramlFile.toPath()));

        generateGoalProcessor.generate(config);

        ArgumentCaptor<Raml> ramlCaptor = forClass(Raml.class);
        verify(generator).run(ramlCaptor.capture(), any(GeneratorConfig.class));

        Raml raml = ramlCaptor.getValue();

        RamlEmitter emitter = new RamlEmitter();
        assertThat(emitter.dump(raml), equalTo("#%RAML 0.8\n"));
    }

    @Test
    public void shouldPassPatternsToFileTreeScanner() throws Exception {

        final String[] customIncludes = {"**/*.txt"};
        final String[] customExcludes = {"**/cheese.txt"};

        when(config.getIncludes()).thenReturn(asList(customIncludes));
        when(config.getExcludes()).thenReturn(asList(customExcludes));

        File ramlFile = sourceDirectory.newFile("file1.raml");
        String ramlString1 = "#%RAML 0.8\nbaseUri: \"http://c:8080/\"\n";
        write(ramlFile, ramlString1);

        when(scanner.find(sourceDirectory.getRoot().toPath(), customIncludes, customExcludes))
                .thenReturn(singletonList(ramlFile.toPath()));

        generateGoalProcessor.generate(config);

        ArgumentCaptor<Raml> ramlCaptor = forClass(Raml.class);
        verify(generator).run(ramlCaptor.capture(), any(GeneratorConfig.class));

        Raml raml = ramlCaptor.getValue();

        RamlEmitter emitter = new RamlEmitter();
        assertThat(emitter.dump(raml), equalTo(ramlString1));
    }
}

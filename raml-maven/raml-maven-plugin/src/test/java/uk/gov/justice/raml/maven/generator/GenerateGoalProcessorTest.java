package uk.gov.justice.raml.maven.generator;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.apache.commons.io.FileUtils.write;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import uk.gov.justice.raml.core.Generator;
import uk.gov.justice.raml.core.GeneratorConfig;
import uk.gov.justice.raml.io.FileTreeScanner;
import uk.gov.justice.raml.io.FileTreeScannerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raml.emitter.RamlEmitter;
import org.raml.model.Raml;

/**
 * Unit tests for the {@link GenerateGoalProcessor} class.
 */
@ExtendWith(MockitoExtension.class)
public class GenerateGoalProcessorTest {

    private final String[] includes = {"**/*.raml"};
    private final String[] excludes = {};

    @TempDir
    public File sourceDirectory;

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

    @InjectMocks
    private GenerateGoalProcessor generateGoalProcessor;
    
    @Test
    @SuppressWarnings("unchecked")
    public void shouldCallGeneratorWithRamlFilesAndConfig() throws Exception {


        final File ramlFile = new File(sourceDirectory, "file1.raml");
        final File ramlFile2 = new File(sourceDirectory, "file2.raml");

        assertThat(ramlFile.createNewFile(), is(true));
        assertThat(ramlFile2.createNewFile(), is(true));

        final String ramlString1 = "#%RAML 0.8\nbaseUri: \"http://a:8080/\"\n";
        write(ramlFile, ramlString1);
        final String ramlString2 = "#%RAML 0.8\nbaseUri: \"http://b:8080/\"\n";
        write(ramlFile2, ramlString2);


        final String generatorName = "mock.generator";

        when(generatorFactory.instanceOf(generatorName)).thenReturn(generator);
        when(config.getGeneratorName()).thenReturn(generatorName);
        when(config.getSourceDirectory()).thenReturn(sourceDirectory.toPath());
        when(config.getIncludes()).thenReturn(asList(includes));
        when(config.getExcludes()).thenReturn(asList(excludes));
        when(scannerFactory.create()).thenReturn(scanner);

        when(scanner.find(sourceDirectory.toPath(), includes, excludes))
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

        final File ramlFile = new File(sourceDirectory, "file3.raml");
        assertThat(ramlFile.createNewFile(), is(true));

        final String generatorName = "mock.generator";

        when(generatorFactory.instanceOf(generatorName)).thenReturn(generator);
        when(config.getGeneratorName()).thenReturn(generatorName);
        when(config.getSourceDirectory()).thenReturn(sourceDirectory.toPath());
        when(config.getIncludes()).thenReturn(asList(includes));
        when(config.getExcludes()).thenReturn(asList(excludes));
        when(scannerFactory.create()).thenReturn(scanner);

        when(scanner.find(sourceDirectory.toPath(), includes, excludes))
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

        final String generatorName = "mock.generator";

        when(generatorFactory.instanceOf(generatorName)).thenReturn(generator);
        when(config.getGeneratorName()).thenReturn(generatorName);
        when(config.getSourceDirectory()).thenReturn(sourceDirectory.toPath());
        when(config.getIncludes()).thenReturn(asList(includes));
        when(config.getExcludes()).thenReturn(asList(excludes));
        when(scannerFactory.create()).thenReturn(scanner);

        when(config.getIncludes()).thenReturn(asList(customIncludes));
        when(config.getExcludes()).thenReturn(asList(customExcludes));

        final File ramlFile = new File(sourceDirectory, "file1.raml");
        assertThat(ramlFile.createNewFile(), is(true));

        final String ramlString1 = "#%RAML 0.8\nbaseUri: \"http://c:8080/\"\n";
        write(ramlFile, ramlString1);

        when(scanner.find(sourceDirectory.toPath(), customIncludes, customExcludes))
                .thenReturn(singletonList(ramlFile.toPath()));

        generateGoalProcessor.generate(config);

        ArgumentCaptor<Raml> ramlCaptor = forClass(Raml.class);
        verify(generator).run(ramlCaptor.capture(), any(GeneratorConfig.class));

        Raml raml = ramlCaptor.getValue();

        RamlEmitter emitter = new RamlEmitter();
        assertThat(emitter.dump(raml), equalTo(ramlString1));
    }

    @Test
    public void shouldNotInstantiateGeneratorIfNoRamlFilesToProcess() throws IOException {

        when(config.getSourceDirectory()).thenReturn(sourceDirectory.toPath());
        when(config.getIncludes()).thenReturn(asList(includes));
        when(config.getExcludes()).thenReturn(asList(excludes));
        when(scannerFactory.create()).thenReturn(scanner);

        when(scanner.find(any(Path.class), any(String[].class), any(String[].class))).thenReturn(emptyList());
        generateGoalProcessor.generate(config);

        verifyNoInteractions(generatorFactory);

    }
}

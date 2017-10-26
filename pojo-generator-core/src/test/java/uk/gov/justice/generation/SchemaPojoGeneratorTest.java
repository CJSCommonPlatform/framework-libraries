package uk.gov.justice.generation;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.SchemaPojoGenerator.ROOT_FIELD_NAME;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.validation.SchemaValidatorVisitor;
import uk.gov.justice.generation.pojo.visitable.Visitable;
import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorService;
import uk.gov.justice.generation.pojo.write.JavaClassFileWriter;
import uk.gov.justice.generation.provider.DefinitionsFactory;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.File;
import java.util.List;

import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class SchemaPojoGeneratorTest {

    @Mock
    private JavaClassFileWriter javaClassFileWriter;

    @Mock
    private DefinitionsFactory definitionsFactory;

    @Mock
    private SchemaLoader schemaLoader;

    @Mock
    private VisitableFactory visitableFactory;

    @Mock
    private AcceptorService acceptorService;

    @Mock
    private Bootstrapper bootstrapper;

    @Mock
    private SchemaValidatorVisitor schemaValidatorVisitor;

    @InjectMocks
    private SchemaPojoGenerator schemaPojoGenerator;

    @Test
    @SuppressWarnings("unchecked")
    public void shouldGeneratePojoFromSchema() throws Exception {

        final String jsonSchemaFileName = "jsonSchemaFileName";

        final File jsonSchemaFile = mock(File.class);
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);
        final Schema jsonSchema = mock(Schema.class);

        final PojoGeneratorProperties generatorProperties = mock(PojoGeneratorProperties.class);
        final GenerationContext generationContext = mock(GenerationContext.class);

        final PluginProvider pluginProvider = mock(PluginProvider.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        final JavaGeneratorFactory javaGeneratorFactory = mock(JavaGeneratorFactory.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);

        final List<Definition> definitions = mock(List.class);
        final List<ClassGeneratable> classGenerators = mock(List.class);

        final Visitable visitable = mock(Visitable.class);

        when(schemaLoader.loadFrom(jsonSchemaFile)).thenReturn(jsonSchema);
        when(generatorConfig.getGeneratorProperties()).thenReturn(generatorProperties);
        when(jsonSchemaFile.getName()).thenReturn(jsonSchemaFileName);

        when(bootstrapper.getGenerationContext(generatorConfig, jsonSchema, jsonSchemaFileName)).thenReturn(generationContext);
        when(bootstrapper.getPluginProvider(generatorProperties)).thenReturn(pluginProvider);
        when(bootstrapper.getClassNameFactory(generationContext, pluginProvider)).thenReturn(classNameFactory);
        when(bootstrapper.getJavaGeneratorFactory(classNameFactory)).thenReturn(javaGeneratorFactory);
        when(bootstrapper.getPluginContext(
                generatorProperties,
                generationContext,
                pluginProvider,
                classNameFactory,
                javaGeneratorFactory)).thenReturn(pluginContext);

        when(generationContext.getLoggerFor(SchemaPojoGenerator.class)).thenReturn(mock(Logger.class));
        when(visitableFactory.createWith(ROOT_FIELD_NAME, jsonSchema, acceptorService)).thenReturn(visitable);
        when(definitionsFactory.createDefinitions(visitable)).thenReturn(definitions);

        when(javaGeneratorFactory.createClassGeneratorsFor(
                definitions,
                pluginProvider,
                pluginContext,
                generationContext
        )).thenReturn(classGenerators);

        schemaPojoGenerator.run(jsonSchemaFile, generatorConfig);

        final InOrder inOrder = inOrder(visitable, javaClassFileWriter);

        inOrder.verify(visitable, times(1)).accept(schemaValidatorVisitor);
        inOrder.verify(javaClassFileWriter, times(1)).writeJavaClassesToFile(generationContext, classGenerators);
    }
}

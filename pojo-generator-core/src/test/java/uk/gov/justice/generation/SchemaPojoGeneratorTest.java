package uk.gov.justice.generation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.plugin.PluginProviderFactory;
import uk.gov.justice.generation.pojo.write.JavaClassFileWriter;
import uk.gov.justice.generation.provider.DefinitionProvider;
import uk.gov.justice.generation.provider.GeneratorContextProvider;
import uk.gov.justice.generation.provider.PluginContextProvider;
import uk.gov.justice.generation.provider.PojoGeneratorFactoriesProvider;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.File;
import java.util.List;

import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class SchemaPojoGeneratorTest {

    @Mock
    private GeneratorContextProvider generatorContextProvider;

    @Mock
    private PojoGeneratorFactoriesProvider pojoGeneratorFactoriesProvider;

    @Mock
    private JavaClassFileWriter javaClassFileWriter;

    @Mock
    private PluginProviderFactory pluginProviderFactory;

    @Mock
    private DefinitionProvider definitionProvider;

    @Mock
    private PluginContextProvider pluginContextProvider;

    @Mock
    private SchemaLoader schemaLoader;

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

        when(schemaLoader.loadFrom(jsonSchemaFile)).thenReturn(jsonSchema);
        when(jsonSchemaFile.getName()).thenReturn(jsonSchemaFileName);
        when(generatorConfig.getGeneratorProperties()).thenReturn(generatorProperties);
        when(generatorContextProvider.create(jsonSchemaFileName, jsonSchema, generatorConfig)).thenReturn(generationContext);

        when(pluginProviderFactory.createFor(generatorProperties)).thenReturn(pluginProvider);
        when(generationContext.getLoggerFor(SchemaPojoGenerator.class)).thenReturn(mock(Logger.class));

        when(pojoGeneratorFactoriesProvider.create(generationContext, pluginProvider)).thenReturn(classNameFactory);
        when(pojoGeneratorFactoriesProvider.create(classNameFactory)).thenReturn(javaGeneratorFactory);

        when(pluginContextProvider.create(
                javaGeneratorFactory,
                classNameFactory,
                generationContext.getSourceFilename(),
                pluginProvider.classModifyingPlugins(),
                generatorProperties
        )).thenReturn(pluginContext);

        when(definitionProvider.createDefinitions(
                jsonSchema,
                jsonSchemaFileName,
                pluginProvider,
                pluginContext
        )).thenReturn(definitions);

        when(javaGeneratorFactory.createClassGeneratorsFor(
                definitions,
                pluginProvider,
                pluginContext,
                generationContext
        )).thenReturn(classGenerators);

        schemaPojoGenerator.run(jsonSchemaFile, generatorConfig);

        verify(javaClassFileWriter, times(1)).writeJavaClassesToFile(generationContext, classGenerators);
    }
}

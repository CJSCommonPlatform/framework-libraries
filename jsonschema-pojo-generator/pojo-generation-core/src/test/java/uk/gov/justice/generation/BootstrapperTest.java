package uk.gov.justice.generation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.plugin.PluginProviderFactory;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.provider.GeneratorContextProvider;
import uk.gov.justice.generation.provider.PluginContextProvider;
import uk.gov.justice.generation.provider.PojoGeneratorFactoriesProvider;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.util.Collections;
import java.util.List;

import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class BootstrapperTest {

    @Mock
    private PluginContextProvider pluginContextProvider;

    @Mock
    private PojoGeneratorFactoriesProvider pojoGeneratorFactoriesProvider;

    @Mock
    private PluginProviderFactory pluginProviderFactory;

    @Mock
    private GeneratorContextProvider generatorContextProvider;

    @InjectMocks
    private Bootstrapper bootstrapper;

    @Test
    public void shouldCreateANewPluginContext() throws Exception {

        final String sourceFilename = "json-schema.json";
        final int serialVersionUID = 23;

        final PojoGeneratorProperties generatorProperties = mock(PojoGeneratorProperties.class);
        final GenerationContext generationContext = mock(GenerationContext.class);
        final PluginProvider pluginProvider = mock(PluginProvider.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final JavaGeneratorFactory javaGeneratorFactory = mock(JavaGeneratorFactory.class);
        final List<ClassModifyingPlugin> classModifyingPlugins = Collections.singletonList(mock(ClassModifyingPlugin.class));
        final PluginContext pluginContext = mock(PluginContext.class);

        when(generationContext.getSourceFilename()).thenReturn(sourceFilename);
        when(pluginProvider.classModifyingPlugins()).thenReturn(classModifyingPlugins);

        when(pluginContextProvider.create(
                javaGeneratorFactory,
                classNameFactory,
                sourceFilename,
                classModifyingPlugins,
                generatorProperties,
                serialVersionUID)).thenReturn(pluginContext);

        assertThat(bootstrapper.getPluginContext(
                generatorProperties,
                generationContext,
                pluginProvider,
                classNameFactory,
                javaGeneratorFactory,
                serialVersionUID), is(pluginContext));
    }

    @Test
    public void shouldCreateANewJavaGeneratorFactory() throws Exception {

        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final JavaGeneratorFactory javaGeneratorFactory = mock(JavaGeneratorFactory.class);

        when(pojoGeneratorFactoriesProvider.createJavaGeneratorFactory(classNameFactory)).thenReturn(javaGeneratorFactory);

        assertThat(bootstrapper.getJavaGeneratorFactory(classNameFactory), is(javaGeneratorFactory));
    }

    @Test
    public void shouldCreateANewClassNameFactory() throws Exception {

        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final GenerationContext generationContext = mock(GenerationContext.class);
        final PluginProvider pluginProvider = mock(PluginProvider.class);

        when(pojoGeneratorFactoriesProvider.createClassNameFactory(generationContext, pluginProvider)).thenReturn(classNameFactory);

        assertThat(bootstrapper.getClassNameFactory(generationContext, pluginProvider), is(classNameFactory));
    }

    @Test
    public void shouldCreateANewPluginProvider() throws Exception {

        final PluginProvider pluginProvider = mock(PluginProvider.class);
        final PojoGeneratorProperties generatorProperties = mock(PojoGeneratorProperties.class);

        when(pluginProviderFactory.createFor(generatorProperties)).thenReturn(pluginProvider);

        assertThat(bootstrapper.getPluginProvider(generatorProperties), is(pluginProvider));
    }

    @Test
    public void shouldCreateANewGenerationContext() throws Exception {

        final String schemaFileName = "json-schema.json";

        final GenerationContext generationContext = mock(GenerationContext.class);
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);
        final Schema schema = mock(Schema.class);

        when(generatorContextProvider.create(schemaFileName, schema, generatorConfig)).thenReturn(generationContext);

        assertThat(bootstrapper.getGenerationContext(generatorConfig, schema, schemaFileName), is(generationContext));
    }
}

package uk.gov.justice.generation.pojo.plugin.classmodifying;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.classmodifying.properties.AdditionalPropertiesDeterminer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

public class PluginContextTest {

    private static final JavaGeneratorFactory UNSPECIFIED_GENERATOR_FACTORY = null;
    private static final ClassNameFactory UNSPECIFIED_CLASS_NAME_FACTORY = null;
    private static final String BLANK = "";
    private static final List<ClassModifyingPlugin> EMPTY_CLASS_MODIFYING_PLUGINS = emptyList();
    private static final Map<String, String> EMPTY_GENERATOR_PROPERTIES = new HashMap<>();

    @Test
    public void shouldReturnJavaGeneratorFactory() throws Exception {
        final JavaGeneratorFactory generatorFactory = mock(JavaGeneratorFactory.class);

        final PluginContext pluginContext = new PluginContext(
                generatorFactory,
                UNSPECIFIED_CLASS_NAME_FACTORY,
                BLANK,
                EMPTY_CLASS_MODIFYING_PLUGINS,
                EMPTY_GENERATOR_PROPERTIES);

        assertThat(pluginContext.getJavaGeneratorFactory(), is(generatorFactory));
    }

    @Test
    public void shouldReturnClassNameFactory() throws Exception {
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);

        final PluginContext pluginContext = new PluginContext(
                UNSPECIFIED_GENERATOR_FACTORY,
                classNameFactory,
                BLANK,
                EMPTY_CLASS_MODIFYING_PLUGINS,
                EMPTY_GENERATOR_PROPERTIES);

        assertThat(pluginContext.getClassNameFactory(), is(classNameFactory));
    }

    @Test
    public void shouldReturnSourceFilename() throws Exception {
        final String sourceFilename = "sourceFilename";

        final PluginContext pluginContext = new PluginContext(
                UNSPECIFIED_GENERATOR_FACTORY,
                UNSPECIFIED_CLASS_NAME_FACTORY,
                sourceFilename,
                EMPTY_CLASS_MODIFYING_PLUGINS,
                EMPTY_GENERATOR_PROPERTIES);

        assertThat(pluginContext.getSourceFilename(), is(sourceFilename));
    }

    @Test
    public void shouldReturnTrueIfTheSpecifiedPluginIsInUseInTheApplication() throws Exception {

        final List<ClassModifyingPlugin> classModifyingPlugins = asList(
                new AddToStringMethodToClassPlugin(new AdditionalPropertiesDeterminer()),
                new MakeClassSerializablePlugin()
        );

        final PluginContext pluginContext = new PluginContext(
                UNSPECIFIED_GENERATOR_FACTORY,
                UNSPECIFIED_CLASS_NAME_FACTORY,
                BLANK,
                classModifyingPlugins,
                EMPTY_GENERATOR_PROPERTIES);

        assertThat(pluginContext.isPluginInUse(AddToStringMethodToClassPlugin.class), is(true));
        assertThat(pluginContext.isPluginInUse(MakeClassSerializablePlugin.class), is(true));
        assertThat(pluginContext.isPluginInUse(AddAdditionalPropertiesToClassPlugin.class), is(false));
    }

    @Test
    public void shouldReturnPropertyValueIfRequestedGeneratorPropertyIsPresent() throws Exception {
        final String propertyName = "testProperty";
        final String propertyValue = "test value";

        final HashMap<String, String> generatorProperties = new HashMap<>();
        generatorProperties.put(propertyName, propertyValue);

        final PluginContext pluginContext = new PluginContext(
                UNSPECIFIED_GENERATOR_FACTORY,
                UNSPECIFIED_CLASS_NAME_FACTORY,
                BLANK,
                EMPTY_CLASS_MODIFYING_PLUGINS,
                generatorProperties);

        assertThat(pluginContext.generatorPropertyValueOf(propertyName), is(Optional.of(propertyValue)));
    }

    @Test
    public void shouldReturnOptionaEmptyIfRequestedGeneratorPropertyIsNotPresent() throws Exception {
        final String propertyName = "testProperty";

        final HashMap<String, String> generatorProperties = new HashMap<>();

        final PluginContext pluginContext = new PluginContext(
                UNSPECIFIED_GENERATOR_FACTORY,
                UNSPECIFIED_CLASS_NAME_FACTORY,
                BLANK,
                EMPTY_CLASS_MODIFYING_PLUGINS,
                generatorProperties);

        assertThat(pluginContext.generatorPropertyValueOf(propertyName), is(Optional.empty()));
    }
}

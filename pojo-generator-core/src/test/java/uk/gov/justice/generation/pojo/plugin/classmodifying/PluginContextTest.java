package uk.gov.justice.generation.pojo.plugin.classmodifying;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.properties.AdditionalPropertiesDeterminer;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

public class PluginContextTest {

    private static final JavaGeneratorFactory UNSPECIFIED_GENERATOR_FACTORY = null;
    private static final ClassNameFactory UNSPECIFIED_CLASS_NAME_FACTORY = null;
    private static final String BLANK = "";
    private static final List<ClassModifyingPlugin> EMPTY_CLASS_MODIFYING_PLUGINS = emptyList();
    private static final PojoGeneratorProperties EMPTY_GENERATOR_PROPERTIES = new PojoGeneratorProperties();

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
    public void shouldReturnPropertyValueIfRequestedTypeMappingsIsPresent() throws Exception {
        final String propertyName = "testProperty";
        final String propertyValue = "test value";

        final ImmutableMap<String, String> typeMappings = of(propertyName, propertyValue);
        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withTypeMappings(typeMappings)
                .build();

        final PluginContext pluginContext = new PluginContext(
                UNSPECIFIED_GENERATOR_FACTORY,
                UNSPECIFIED_CLASS_NAME_FACTORY,
                BLANK,
                EMPTY_CLASS_MODIFYING_PLUGINS,
                generatorProperties);

        assertThat(pluginContext.typeMappingOf(propertyName), is(Optional.of(propertyValue)));
    }

    @Test
    public void shouldReturnOptionaEmptyIfRequestedTypeMappingIsNotPresent() throws Exception {
        final String propertyName = "testProperty";

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withTypeMappings(new HashMap<>())
                .build();

        final PluginContext pluginContext = new PluginContext(
                UNSPECIFIED_GENERATOR_FACTORY,
                UNSPECIFIED_CLASS_NAME_FACTORY,
                BLANK,
                EMPTY_CLASS_MODIFYING_PLUGINS,
                generatorProperties);

        assertThat(pluginContext.typeMappingOf(propertyName), is(Optional.empty()));
    }

    @Test
    public void shouldGetTheSetOfTypeMappingNames() throws Exception {

        final ImmutableMap<String, String> typeMappings = of(
                "key_1", "value_1",
                "key_2", "value_2",
                "key_3", "value_3"
        );
        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withTypeMappings(typeMappings)
                .build();

        final PluginContext pluginContext = new PluginContext(
                UNSPECIFIED_GENERATOR_FACTORY,
                UNSPECIFIED_CLASS_NAME_FACTORY,
                BLANK,
                EMPTY_CLASS_MODIFYING_PLUGINS,
                generatorProperties);

        final Set<String> propertyNames = pluginContext.getPropertyNames();

        assertThat(propertyNames.size(), is(3));

        assertThat(propertyNames, hasItem("key_1"));
        assertThat(propertyNames, hasItem("key_2"));
        assertThat(propertyNames, hasItem("key_3"));
    }

    @Test
    public void shouldReturnRootClassName() throws Exception {
        final String rootClassName = "rootClassName";
        final PojoGeneratorProperties generatorProperties = mock(PojoGeneratorProperties.class);

        when(generatorProperties.getRootClassName()).thenReturn(Optional.of(rootClassName));

        final PluginContext pluginContext = new PluginContext(
                UNSPECIFIED_GENERATOR_FACTORY,
                UNSPECIFIED_CLASS_NAME_FACTORY,
                BLANK,
                EMPTY_CLASS_MODIFYING_PLUGINS,
                generatorProperties);

        final Optional<String> result = pluginContext.getRootClassName();

        assertThat(result, is(Optional.of(rootClassName)));
    }
}

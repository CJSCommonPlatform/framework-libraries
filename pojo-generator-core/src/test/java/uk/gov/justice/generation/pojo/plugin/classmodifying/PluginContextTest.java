package uk.gov.justice.generation.pojo.plugin.classmodifying;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.plugin.typemodifying.TypeMappingPredicate.FORMAT_TYPE;
import static uk.gov.justice.generation.pojo.plugin.typemodifying.TypeMappingPredicate.REFERENCE_TYPE;
import static uk.gov.justice.generation.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.properties.AdditionalPropertiesDeterminer;

import java.util.List;
import java.util.Optional;

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
    public void shouldReturnPropertyValueIfRequestedReferenceTypeMappingsIsPresent() throws Exception {
        final String propertyName = "testProperty";
        final String propertyValue = "test value";

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .addReferenceTypeMappingOf(propertyName, propertyValue)
                .build();

        final PluginContext pluginContext = new PluginContext(
                UNSPECIFIED_GENERATOR_FACTORY,
                UNSPECIFIED_CLASS_NAME_FACTORY,
                BLANK,
                EMPTY_CLASS_MODIFYING_PLUGINS,
                generatorProperties);

        assertThat(pluginContext.typeMappingsFilteredBy(REFERENCE_TYPE, propertyName), is(Optional.of(propertyValue)));
    }

    @Test
    public void shouldReturnPropertyValueIfRequestedFormatTypeMappingsIsPresent() throws Exception {
        final String propertyName = "testProperty";
        final String propertyValue = "test value";

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .addFormatTypeMappingOf(propertyName, propertyValue)
                .build();

        final PluginContext pluginContext = new PluginContext(
                UNSPECIFIED_GENERATOR_FACTORY,
                UNSPECIFIED_CLASS_NAME_FACTORY,
                BLANK,
                EMPTY_CLASS_MODIFYING_PLUGINS,
                generatorProperties);

        assertThat(pluginContext.typeMappingsFilteredBy(FORMAT_TYPE, propertyName), is(Optional.of(propertyValue)));
    }

    @Test
    public void shouldReturnOptionaEmptyIfRequestedTypeMappingIsNotPresent() throws Exception {
        final String propertyName = "testProperty";

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .build();

        final PluginContext pluginContext = new PluginContext(
                UNSPECIFIED_GENERATOR_FACTORY,
                UNSPECIFIED_CLASS_NAME_FACTORY,
                BLANK,
                EMPTY_CLASS_MODIFYING_PLUGINS,
                generatorProperties);

        assertThat(pluginContext.typeMappingsFilteredBy(REFERENCE_TYPE, propertyName), is(Optional.empty()));
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

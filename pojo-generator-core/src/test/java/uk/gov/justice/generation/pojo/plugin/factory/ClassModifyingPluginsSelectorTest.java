package uk.gov.justice.generation.pojo.plugin.factory;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.plugin.Plugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddAdditionalPropertiesToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddFieldsAndMethodsToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddHashcodeAndEqualsPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.GenerateBuilderForClassPlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.CustomReturnTypePlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.SupportJavaOptionalsPlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassModifyingPluginsSelectorTest {

    @Mock
    private DefaultPluginsProvider defaultPluginsProvider;

    @InjectMocks
    private ClassModifyingPluginsSelector classModifyingPluginsSelector;

    @Test
    public void shouldGetTheListOfClassModifyingPluginsWithDefaultPluginsIfExcludeDefaultPluginsIsFalse() throws Exception {

        final PojoGeneratorProperties generatorProperties = mock(PojoGeneratorProperties.class);
        final Map<Class<?>, List<Plugin>> pluginTypes = new HashMap<>();

        pluginTypes.put(ClassModifyingPlugin.class, classModifyingPlugins());
        pluginTypes.put(TypeModifyingPlugin.class, typeModifyingPlugins());

        when(generatorProperties.isExcludeDefaultPlugins()).thenReturn(false);
        when(defaultPluginsProvider.getDefaultPlugins()).thenReturn(defaultPlugins());

        final List<ClassModifyingPlugin> classModifyingPlugins = classModifyingPluginsSelector.selectFrom(
                pluginTypes, generatorProperties
        );

        assertThat(classModifyingPlugins.size(), is(4));
        assertThat(classModifyingPlugins.get(0), is(instanceOf(AddFieldsAndMethodsToClassPlugin.class)));
        assertThat(classModifyingPlugins.get(1), is(instanceOf(GenerateBuilderForClassPlugin.class)));
        assertThat(classModifyingPlugins.get(2), is(instanceOf(AddAdditionalPropertiesToClassPlugin.class)));
        assertThat(classModifyingPlugins.get(3), is(instanceOf(AddHashcodeAndEqualsPlugin.class)));
    }

    @Test
    public void shouldGetTheListOfClassModifyingPluginsWithoutDefaultPluginsIfExcludeDefaultPluginsIsTrue() throws Exception {

        final PojoGeneratorProperties generatorProperties = mock(PojoGeneratorProperties.class);
        final Map<Class<?>, List<Plugin>> pluginTypes = new HashMap<>();

        pluginTypes.put(ClassModifyingPlugin.class, classModifyingPlugins());
        pluginTypes.put(TypeModifyingPlugin.class, typeModifyingPlugins());

        when(generatorProperties.isExcludeDefaultPlugins()).thenReturn(true);

        final List<ClassModifyingPlugin> classModifyingPlugins = classModifyingPluginsSelector.selectFrom(
                pluginTypes, generatorProperties
        );

        assertThat(classModifyingPlugins.size(), is(2));
        assertThat(classModifyingPlugins.get(0), is(instanceOf(AddAdditionalPropertiesToClassPlugin.class)));
        assertThat(classModifyingPlugins.get(1), is(instanceOf(AddHashcodeAndEqualsPlugin.class)));

        verifyZeroInteractions(defaultPluginsProvider);
    }

    @Test
    public void shouldGetDefaultPluginsIfThereAreNoUserDefinedClassModifyingPlugins() throws Exception {

        final PojoGeneratorProperties generatorProperties = mock(PojoGeneratorProperties.class);
        final Map<Class<?>, List<Plugin>> pluginTypes = new HashMap<>();

        pluginTypes.put(TypeModifyingPlugin.class, typeModifyingPlugins());

        when(generatorProperties.isExcludeDefaultPlugins()).thenReturn(false);
        when(defaultPluginsProvider.getDefaultPlugins()).thenReturn(defaultPlugins());

        final List<ClassModifyingPlugin> classModifyingPlugins = classModifyingPluginsSelector.selectFrom(
                pluginTypes, generatorProperties
        );

        assertThat(classModifyingPlugins.size(), is(2));
        assertThat(classModifyingPlugins.get(0), is(instanceOf(AddFieldsAndMethodsToClassPlugin.class)));
        assertThat(classModifyingPlugins.get(1), is(instanceOf(GenerateBuilderForClassPlugin.class)));
    }

    private List<Plugin> classModifyingPlugins() {
        final Plugin addAdditionalPropertiesToClassPlugin = mock(AddAdditionalPropertiesToClassPlugin.class);
        final Plugin addHashcodeAndEqualsPlugin = mock(AddHashcodeAndEqualsPlugin.class);
        return asList(addAdditionalPropertiesToClassPlugin, addHashcodeAndEqualsPlugin);
    }

    private List<Plugin> typeModifyingPlugins() {
        final Plugin customReturnTypePlugin = mock(CustomReturnTypePlugin.class);
        final Plugin supportJavaOptionalsPlugin = mock(SupportJavaOptionalsPlugin.class);
        return asList(customReturnTypePlugin, supportJavaOptionalsPlugin);
    }

    private List<ClassModifyingPlugin> defaultPlugins() {
        final ClassModifyingPlugin addFieldsAndMethodsToClassPlugin = mock(AddFieldsAndMethodsToClassPlugin.class);
        final ClassModifyingPlugin generateBuilderForClassPlugin = mock(GenerateBuilderForClassPlugin.class);
        return asList(addFieldsAndMethodsToClassPlugin, generateBuilderForClassPlugin);
    }
}

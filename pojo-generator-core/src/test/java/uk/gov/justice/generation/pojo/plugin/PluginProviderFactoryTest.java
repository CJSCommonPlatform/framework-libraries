package uk.gov.justice.generation.pojo.plugin;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.plugin.classmodifying.AddFieldsAndMethodsToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.GenerateBuilderForClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.MakeClassSerializablePlugin;
import uk.gov.justice.generation.pojo.plugin.namegeneratable.RootNameGeneratorPlugin;
import uk.gov.justice.generation.pojo.plugin.testplugins.UserDefinedNameGeneratorPlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.CustomReturnTypePlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.SupportJavaOptionalsPlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PluginProviderFactoryTest {

    private static final String EXCLUDE_DEFAULT_PLUGINS_PROPERTY = "excludeDefaultPlugins";
    private static final String PLUGINS_PROPERTY = "plugins";

    private static final String MULTIPLE_NAME_GENERATBLE_PLUGINS =
                    UserDefinedNameGeneratorPlugin.class.getName() + ",\n" +
                    UserDefinedNameGeneratorPlugin.class.getName();


    private static final String MULTIPLE_TYPES_OF_PLUGIN =
                    CustomReturnTypePlugin.class.getName() + ",\n" +
                    SupportJavaOptionalsPlugin.class.getName() + ",\n" +
                    MakeClassSerializablePlugin.class.getName() + ",\n" +
                    UserDefinedNameGeneratorPlugin.class.getName();

    private static final String BLANK = "";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnProviderWithDefaultClassModifyingPlugins() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final PluginProvider pluginProvider = new PluginProviderFactory().createFor(generatorConfig);

        assertThat(pluginProvider.classModifyingPlugins().size(), is(2));
        assertThat(pluginProvider.classModifyingPlugins(), hasItems(
                instanceOf(AddFieldsAndMethodsToClassPlugin.class),
                instanceOf(GenerateBuilderForClassPlugin.class)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnProviderWithNoDefaultsIfExcludeDefaultPluginsPropertyIsSetToTrue() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put(EXCLUDE_DEFAULT_PLUGINS_PROPERTY, "true");

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final PluginProvider pluginProvider = new PluginProviderFactory().createFor(generatorConfig);

        assertThat(pluginProvider.classModifyingPlugins().size(), is(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnProviderWithDefaultsIfExcludeDefaultPluginsPropertyIsSetToFalse() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put(EXCLUDE_DEFAULT_PLUGINS_PROPERTY, "false");

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final PluginProvider pluginProvider = new PluginProviderFactory().createFor(generatorConfig);

        assertThat(pluginProvider.classModifyingPlugins().size(), is(2));
        assertThat(pluginProvider.classModifyingPlugins(), hasItems(
                instanceOf(AddFieldsAndMethodsToClassPlugin.class),
                instanceOf(GenerateBuilderForClassPlugin.class)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnProviderWithDefaultsAndUserDefinedClassModifyingPlugin() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put(PLUGINS_PROPERTY, MakeClassSerializablePlugin.class.getName());

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final PluginProvider pluginProvider = new PluginProviderFactory().createFor(generatorConfig);

        assertThat(pluginProvider.classModifyingPlugins().size(), is(3));
        assertThat(pluginProvider.classModifyingPlugins(), hasItems(
                instanceOf(AddFieldsAndMethodsToClassPlugin.class),
                instanceOf(GenerateBuilderForClassPlugin.class),
                instanceOf(MakeClassSerializablePlugin.class)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnProviderWithDefaultForBlankProperty() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put(PLUGINS_PROPERTY, BLANK);

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final PluginProvider pluginProvider = new PluginProviderFactory().createFor(generatorConfig);

        assertThat(pluginProvider.classModifyingPlugins().size(), is(2));
        assertThat(pluginProvider.classModifyingPlugins(), hasItems(
                instanceOf(AddFieldsAndMethodsToClassPlugin.class),
                instanceOf(GenerateBuilderForClassPlugin.class)));
    }

    @Test
    public void shouldReturnProviderWithNoTypeModifyingPlugins() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final PluginProvider pluginProvider = new PluginProviderFactory().createFor(generatorConfig);

        assertThat(pluginProvider.typeModifyingPlugins().size(), is(0));
    }

    @Test
    public void shouldReturnProviderWithSingleTypeModifyingPlugin() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put(PLUGINS_PROPERTY, CustomReturnTypePlugin.class.getName());

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final PluginProvider pluginProvider = new PluginProviderFactory().createFor(generatorConfig);

        assertThat(pluginProvider.typeModifyingPlugins().size(), is(1));
        assertThat(pluginProvider.typeModifyingPlugins(), hasItem(instanceOf(CustomReturnTypePlugin.class)));
    }

    @Test
    public void shouldReturnProviderWithDefaultNameGeneratablePlugin() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final PluginProvider pluginProvider = new PluginProviderFactory().createFor(generatorConfig);

        assertThat(pluginProvider.nameGeneratablePlugin(), is(instanceOf(RootNameGeneratorPlugin.class)));
    }

    @Test
    public void shouldReturnProviderWithUserDefinedNameGeneratablePlugin() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put(PLUGINS_PROPERTY, UserDefinedNameGeneratorPlugin.class.getName());

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final PluginProvider pluginProvider = new PluginProviderFactory().createFor(generatorConfig);

        assertThat(pluginProvider.nameGeneratablePlugin(), is(instanceOf(UserDefinedNameGeneratorPlugin.class)));
    }

    @Test
    public void shouldTrhowExceptionIfMoreThanOneUserDefinedNameGeneratablePlugin() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put(PLUGINS_PROPERTY, MULTIPLE_NAME_GENERATBLE_PLUGINS);

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        expectedException.expect(PluginProviderException.class);
        expectedException.expectMessage("Multiple NameGeneratablePlugin identified, please supply only one. List: [UserDefinedNameGeneratorPlugin, UserDefinedNameGeneratorPlugin]");

        new PluginProviderFactory().createFor(generatorConfig);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnProviderWithMultipleTypeModifyingPlugin() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put(PLUGINS_PROPERTY, CustomReturnTypePlugin.class.getName() + ",\n" +
                SupportJavaOptionalsPlugin.class.getName());

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final PluginProvider pluginProvider = new PluginProviderFactory().createFor(generatorConfig);

        final List<TypeModifyingPlugin> typeModifyingPlugins = pluginProvider.typeModifyingPlugins();

        assertThat(typeModifyingPlugins.size(), is(2));
        assertThat(typeModifyingPlugins, hasItems(
                instanceOf(CustomReturnTypePlugin.class),
                instanceOf(SupportJavaOptionalsPlugin.class)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnProviderWithMultipleTypeModifyingPluginAndClassModifyingPlugin() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put(PLUGINS_PROPERTY, MULTIPLE_TYPES_OF_PLUGIN);

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final PluginProvider pluginProvider = new PluginProviderFactory().createFor(generatorConfig);

        assertThat(pluginProvider.typeModifyingPlugins().size(), is(2));
        assertThat(pluginProvider.typeModifyingPlugins(), hasItems(
                instanceOf(CustomReturnTypePlugin.class),
                instanceOf(SupportJavaOptionalsPlugin.class)));

        assertThat(pluginProvider.classModifyingPlugins().size(), is(3));
        assertThat(pluginProvider.classModifyingPlugins(), hasItems(
                instanceOf(AddFieldsAndMethodsToClassPlugin.class),
                instanceOf(GenerateBuilderForClassPlugin.class),
                instanceOf(MakeClassSerializablePlugin.class)));

        assertThat(pluginProvider.nameGeneratablePlugin(), is(instanceOf(UserDefinedNameGeneratorPlugin.class)));
    }

    @Test
    public void shouldThrowExceptionIfUserDefinedPluginDoesNotImplementPluginInterface() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put(PLUGINS_PROPERTY, "uk.gov.justice.generation.pojo.plugin.testplugins.TestNotPlugin");

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        expectedException.expect(PluginProviderException.class);
        expectedException.expectMessage("Incorrect Class Type, Class name: uk.gov.justice.generation.pojo.plugin.testplugins.TestNotPlugin, does not implement ClassModifyingPlugin or TypeModifyingPlugin or NameGeneratablePlugin.");

        new PluginProviderFactory().createFor(generatorConfig);
    }

    @Test
    public void shouldThrowExceptionIfUserDefinedPluginCanNotBeInstantiated() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put(PLUGINS_PROPERTY, "uk.gov.justice.generation.pojo.plugin.testplugins.TestAbstractPlugin");

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        expectedException.expect(PluginProviderException.class);
        expectedException.expectMessage("Unable to create instance of pojo plugin with class name uk.gov.justice.generation.pojo.plugin.testplugins.TestAbstractPlugin");
        expectedException.expectCause(instanceOf(InstantiationException.class));

        new PluginProviderFactory().createFor(generatorConfig);
    }

    @Test
    public void shouldThrowExceptionIfUserDefinedPluginCanNotBeFound() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put(PLUGINS_PROPERTY, "uk.gov.justice.generation.pojo.plugin.testplugins.NotFoundPlugin");

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        expectedException.expect(PluginProviderException.class);
        expectedException.expectMessage("Unable to create instance of pojo plugin with class name uk.gov.justice.generation.pojo.plugin.testplugins.NotFoundPlugin");
        expectedException.expectCause(instanceOf(ClassNotFoundException.class));

        new PluginProviderFactory().createFor(generatorConfig);
    }

    @Test
    public void shouldThrowExceptionIfUserDefinedPluginHasPrivateConstructor() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put(PLUGINS_PROPERTY, "uk.gov.justice.generation.pojo.plugin.testplugins.TestPrivateConstructorPlugin");

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        expectedException.expect(PluginProviderException.class);
        expectedException.expectMessage("Unable to create instance of pojo plugin with class name uk.gov.justice.generation.pojo.plugin.testplugins.TestPrivateConstructorPlugin");
        expectedException.expectCause(instanceOf(IllegalAccessException.class));

        new PluginProviderFactory().createFor(generatorConfig);
    }
}

package uk.gov.justice.generation.pojo.generators.plugin;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.generators.plugin.providers.TestPluginProvider;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PluginProviderFactoryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldReturnDefaultPluginProviderIfNoProviderSet() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final PluginProvider pluginProvider = new PluginProviderFactory().createFor(generatorConfig);

        assertThat(pluginProvider, is(instanceOf(DefaultPluginProvider.class)));
    }

    @Test
    public void shouldReturnPluginProviderSetInGeneratorProperties() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put("pojo-plugin-provider", "uk.gov.justice.generation.pojo.generators.plugin.providers.TestPluginProvider");

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final PluginProvider pluginProvider = new PluginProviderFactory().createFor(generatorConfig);

        assertThat(pluginProvider, is(instanceOf(TestPluginProvider.class)));
    }

    @Test
    public void shouldThrowExceptionIfSetClassIsNotPluginProvider() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put("pojo-plugin-provider", "uk.gov.justice.generation.pojo.generators.plugin.providers.TestNotPluginProvider");

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        expectedException.expect(PluginProviderException.class);
        expectedException.expectMessage("Unable to create instance of pojo plugin provider with class name uk.gov.justice.generation.pojo.generators.plugin.providers.TestNotPluginProvider");
        expectedException.expectCause(instanceOf(ClassCastException.class));

        new PluginProviderFactory().createFor(generatorConfig);
    }

    @Test
    public void shouldThrowExceptionIfSetClassCanNotBeInstantiated() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put("pojo-plugin-provider", "uk.gov.justice.generation.pojo.generators.plugin.providers.TestAbstractPluginProvider");

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        expectedException.expect(PluginProviderException.class);
        expectedException.expectMessage("Unable to create instance of pojo plugin provider with class name uk.gov.justice.generation.pojo.generators.plugin.providers.TestAbstractPluginProvider");
        expectedException.expectCause(instanceOf(InstantiationException.class));

        new PluginProviderFactory().createFor(generatorConfig);
    }

    @Test
    public void shouldThrowExceptionIfSetClassCanNotBeFound() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put("pojo-plugin-provider", "uk.gov.justice.generation.pojo.generators.plugin.providers.NotFoundPluginProvider");

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        expectedException.expect(PluginProviderException.class);
        expectedException.expectMessage("Unable to create instance of pojo plugin provider with class name uk.gov.justice.generation.pojo.generators.plugin.providers.NotFoundPluginProvider");
        expectedException.expectCause(instanceOf(ClassNotFoundException.class));

        new PluginProviderFactory().createFor(generatorConfig);
    }

    @Test
    public void shouldThrowExceptionIfSetClassHasPrivateConstructor() throws Exception {
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        final Map<String, String> properties = new HashMap<>();
        properties.put("pojo-plugin-provider", "uk.gov.justice.generation.pojo.generators.plugin.providers.TestPrivateConstructorPlugProvider");

        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        expectedException.expect(PluginProviderException.class);
        expectedException.expectMessage("Unable to create instance of pojo plugin provider with class name uk.gov.justice.generation.pojo.generators.plugin.providers.TestPrivateConstructorPlugProvider");
        expectedException.expectCause(instanceOf(IllegalAccessException.class));

        new PluginProviderFactory().createFor(generatorConfig);
    }
}

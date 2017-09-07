package uk.gov.justice.generation.pojo.generators.plugin;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassGeneratorPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.EventAnnotationPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.FieldAndMethodPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.SerializablePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeNamePlugin;

import java.util.List;

import org.junit.Test;

public class PluginTestProviderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProvideDefaultListOfPluginClassGenerators() throws Exception {
        final List<ClassGeneratorPlugin> pluginClassGenerators = new PluginTestProvider().pluginClassGenerators();

        assertThat(pluginClassGenerators.size(), is(3));
        assertThat(pluginClassGenerators, hasItems(
                instanceOf(EventAnnotationPlugin.class),
                instanceOf(SerializablePlugin.class),
                instanceOf(FieldAndMethodPlugin.class)));
    }

    @Test
    public void shouldProvideDefaultListOfTypeNamePlugins() throws Exception {

        final List<TypeNamePlugin> typeNamePlugins = new PluginTestProvider().typeNamePlugins();

        assertThat(typeNamePlugins.size(), is(0));
    }
}

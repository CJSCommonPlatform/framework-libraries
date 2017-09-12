package uk.gov.justice.generation.pojo.generators.plugin;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.AddEventAnnotationToClassPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.AddFieldsAndMethodsToClassPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.MakeClassSerializablePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeModifyingPlugin;

import java.util.List;

import org.junit.Test;

public class PluginTestProviderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProvideDefaultListOfPluginClassGenerators() throws Exception {
        final List<ClassModifyingPlugin> pluginClassGenerators = new PluginTestProvider().classModifyingPlugins();

        assertThat(pluginClassGenerators.size(), is(3));
        assertThat(pluginClassGenerators, hasItems(
                instanceOf(AddEventAnnotationToClassPlugin.class),
                instanceOf(MakeClassSerializablePlugin.class),
                instanceOf(AddFieldsAndMethodsToClassPlugin.class)));
    }

    @Test
    public void shouldProvideDefaultListOfTypeNamePlugins() throws Exception {

        final List<TypeModifyingPlugin> typeModifyingPlugins = new PluginTestProvider().typeModifyingPlugins();

        assertThat(typeModifyingPlugins.size(), is(0));
    }
}

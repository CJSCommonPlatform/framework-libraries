package uk.gov.justice.generation.pojo.generators.plugin;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

public class PluginTestProviderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProvideDefaultListOfPluginClassGenerators() throws Exception {
        final List<PluginClassGeneratable> pluginClassGenerators = new PluginTestProvider().pluginClassGenerators();

        assertThat(pluginClassGenerators.size(), is(3));
        assertThat(pluginClassGenerators, hasItems(
                instanceOf(EventAnnotationGenerator.class),
                instanceOf(SerializableGenerator.class),
                instanceOf(FieldAndMethodGenerator.class)));
    }

    @Test
    public void shouldProvideDefaultListOfTypeNamePlugins() throws Exception {

        final List<TypeNamePlugin> typeNamePlugins = new PluginTestProvider().typeNamePlugins();

        assertThat(typeNamePlugins.size(), is(0));
    }
}

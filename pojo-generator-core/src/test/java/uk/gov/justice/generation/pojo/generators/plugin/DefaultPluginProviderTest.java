package uk.gov.justice.generation.pojo.generators.plugin;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.generators.plugin.builder.BuilderPlugin;

import java.util.List;

import org.junit.Test;

public class DefaultPluginProviderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProvideDefaultListOfPluginClassGenerators() throws Exception {
        final List<PluginClassGeneratable> pluginClassGenerators = new DefaultPluginProvider().pluginClassGenerators();

        assertThat(pluginClassGenerators.size(), is(4));
        assertThat(pluginClassGenerators, hasItems(
                instanceOf(EventAnnotationGenerator.class),
                instanceOf(SerializableGenerator.class),
                instanceOf(FieldAndMethodGenerator.class),
                instanceOf(BuilderPlugin.class)
                )
        );
    }

    @Test
    public void shouldProvideDefaultListOfTypeNamePlugins() throws Exception {

        final List<TypeNamePlugin> typeNamePlugins = new DefaultPluginProvider().typeNamePlugins();

        assertThat(typeNamePlugins.size(), is(1));
        assertThat(typeNamePlugins, hasItem(instanceOf(OptionalTypeNamePlugin.class)));
    }
}

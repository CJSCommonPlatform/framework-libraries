package uk.gov.justice.generation.pojo.generators.plugin;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassGeneratorPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.EventAnnotationPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.FieldAndMethodPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.SerializablePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.builder.BuilderPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.OptionalTypeNamePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeNamePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.UuidTypeNamePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.ZonedDateTimeTypeNamePlugin;

import java.util.List;

import org.junit.Test;

public class DefaultPluginProviderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProvideDefaultListOfPluginClassGenerators() throws Exception {
        final List<ClassGeneratorPlugin> pluginClassGenerators = new DefaultPluginProvider().pluginClassGenerators();

        assertThat(pluginClassGenerators.size(), is(4));
        assertThat(pluginClassGenerators, hasItems(
                instanceOf(EventAnnotationPlugin.class),
                instanceOf(SerializablePlugin.class),
                instanceOf(FieldAndMethodPlugin.class),
                instanceOf(BuilderPlugin.class)
                )
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProvideDefaultListOfTypeNamePlugins() throws Exception {

        final List<TypeNamePlugin> typeNamePlugins = new DefaultPluginProvider().typeNamePlugins();

        assertThat(typeNamePlugins.size(), is(3));
        assertThat(typeNamePlugins, hasItems(
                instanceOf(OptionalTypeNamePlugin.class),
                instanceOf(UuidTypeNamePlugin.class),
                instanceOf(ZonedDateTimeTypeNamePlugin.class)));
    }
}

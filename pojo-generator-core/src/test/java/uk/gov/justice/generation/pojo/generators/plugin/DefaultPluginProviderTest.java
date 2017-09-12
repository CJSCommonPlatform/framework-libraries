package uk.gov.justice.generation.pojo.generators.plugin;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.AddFieldsAndMethodsToClassPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.MakeClassSerializablePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.GenerateBuilderForClassPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.SupportJavaOptionalsPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeModifyingPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.SupportUuidsPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.SupportZonedDateTimePlugin;

import java.util.List;

import org.junit.Test;

public class DefaultPluginProviderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProvideDefaultListOfPluginClassGenerators() throws Exception {
        final List<ClassModifyingPlugin> pluginClassGenerators = new DefaultPluginProvider().classModifyingPlugins();

        assertThat(pluginClassGenerators.size(), is(3));
        assertThat(pluginClassGenerators, hasItems(
                instanceOf(MakeClassSerializablePlugin.class),
                instanceOf(AddFieldsAndMethodsToClassPlugin.class),
                instanceOf(GenerateBuilderForClassPlugin.class)
                )
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProvideDefaultListOfTypeNamePlugins() throws Exception {

        final List<TypeModifyingPlugin> typeModifyingPlugins = new DefaultPluginProvider().typeModifyingPlugins();

        assertThat(typeModifyingPlugins.size(), is(3));
        assertThat(typeModifyingPlugins, hasItems(
                instanceOf(SupportJavaOptionalsPlugin.class),
                instanceOf(SupportUuidsPlugin.class),
                instanceOf(SupportZonedDateTimePlugin.class)));
    }
}

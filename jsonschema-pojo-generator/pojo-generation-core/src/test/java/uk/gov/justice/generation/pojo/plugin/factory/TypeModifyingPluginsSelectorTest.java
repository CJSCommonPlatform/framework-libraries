package uk.gov.justice.generation.pojo.plugin.factory;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import uk.gov.justice.generation.pojo.plugin.Plugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;

import java.util.List;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TypeModifyingPluginsSelectorTest {

    @InjectMocks
    private TypeModifyingPluginsSelector typeModifyingPluginsSelector;

    @Test
    public void shouldSelectTheTypeModifyingPluginsAndReturnAsAList() throws Exception {

        final TypeModifyingPlugin typeModifyingPlugin_1 = mock(TypeModifyingPlugin.class);
        final TypeModifyingPlugin typeModifyingPlugin_2 = mock(TypeModifyingPlugin.class);
        final TypeModifyingPlugin typeModifyingPlugin_3 = mock(TypeModifyingPlugin.class);

        final List<Plugin> typeModifyingPlugins = asList(
                typeModifyingPlugin_1,
                typeModifyingPlugin_2,
                typeModifyingPlugin_3);

        final ClassModifyingPlugin classModifyingPlugin_1 = mock(ClassModifyingPlugin.class);
        final ClassModifyingPlugin classModifyingPlugin_2 = mock(ClassModifyingPlugin.class);
        final ClassModifyingPlugin classModifyingPlugin_3 = mock(ClassModifyingPlugin.class);

        final List<Plugin> classModifyingPlugins = asList(
                classModifyingPlugin_1,
                classModifyingPlugin_2,
                classModifyingPlugin_3
        );

        final ImmutableMap<Class<?>, List<Plugin>> pluginTypes = of(
                TypeModifyingPlugin.class, typeModifyingPlugins,
                ClassModifyingPlugin.class, classModifyingPlugins);

        final List<TypeModifyingPlugin> plugins = typeModifyingPluginsSelector.selectFrom(pluginTypes);

        assertThat(plugins.size(), is(3));
        assertThat(plugins, hasItem(typeModifyingPlugin_1));
        assertThat(plugins, hasItem(typeModifyingPlugin_2));
        assertThat(plugins, hasItem(typeModifyingPlugin_3));
    }

    @Test
    public void shouldSelectTheTypeModifyingPluginsAndReturnEmptyListIfNoKeySet() throws Exception {

        final ClassModifyingPlugin classModifyingPlugin_1 = mock(ClassModifyingPlugin.class);
        final ClassModifyingPlugin classModifyingPlugin_2 = mock(ClassModifyingPlugin.class);
        final ClassModifyingPlugin classModifyingPlugin_3 = mock(ClassModifyingPlugin.class);

        final List<Plugin> classModifyingPlugins = asList(
                classModifyingPlugin_1,
                classModifyingPlugin_2,
                classModifyingPlugin_3
        );

        final ImmutableMap<Class<?>, List<Plugin>> pluginTypes = of(
                ClassModifyingPlugin.class, classModifyingPlugins);

        final List<TypeModifyingPlugin> plugins = typeModifyingPluginsSelector.selectFrom(pluginTypes);

        assertThat(plugins.isEmpty(), is(true));
    }
}

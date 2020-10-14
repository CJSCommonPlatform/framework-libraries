package uk.gov.justice.generation.pojo.plugin;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.generation.pojo.plugin.factory.AllPluginsInstantiator;
import uk.gov.justice.generation.pojo.plugin.factory.ClassModifyingPluginsSelector;
import uk.gov.justice.generation.pojo.plugin.factory.PluginTypeSorter;
import uk.gov.justice.generation.pojo.plugin.factory.PluginsFromClassnameListFactory;
import uk.gov.justice.generation.pojo.plugin.factory.TypeModifyingPluginsSelector;

import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PluginProviderFactoryFactoryTest {

    @InjectMocks
    private PluginProviderFactoryFactory pluginProviderFactoryFactory;

    @Test
    public void shouldCreateThePluginProviderFactory() throws Exception {

        final PluginProviderFactory pluginProviderFactory = pluginProviderFactoryFactory.create();

        assertThat(getFieldFrom(pluginProviderFactory, "classModifyingPluginsSelector"), is(instanceOf(ClassModifyingPluginsSelector.class)));
        assertThat(getFieldFrom(pluginProviderFactory, "typeModifyingPluginsSelector"), is(instanceOf(TypeModifyingPluginsSelector.class)));
        assertThat(getFieldFrom(pluginProviderFactory, "pluginTypeSorter"), is(instanceOf(PluginTypeSorter.class)));
        assertThat(getFieldFrom(pluginProviderFactory, "allPluginsInstantiator"), is(instanceOf(AllPluginsInstantiator.class)));
        assertThat(getFieldFrom(pluginProviderFactory, "parsePluginNames"), is(instanceOf(PluginsFromClassnameListFactory.class)));
        assertThat(getFieldFrom(pluginProviderFactory, "pluginVerifier"), is(instanceOf(PluginVerifier.class)));
    }

    private Object getFieldFrom(final PluginProviderFactory pluginProviderFactory, final String fieldName) throws Exception {
        final Field field = pluginProviderFactory.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);

        return field.get(pluginProviderFactory);
    }


}

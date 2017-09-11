package uk.gov.justice.generation.pojo.generators.plugin;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeModifyingPlugin;

import java.util.List;

import org.junit.Test;

public class ModifyingPluginProviderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnListOfClassModifyingPlugins() throws Exception {
        final List<TypeModifyingPlugin> undefined = null;
        final List<ClassModifyingPlugin> classModifyingPlugins = mock(List.class);

        final ModifyingPluginProvider modifyingPluginProvider = new ModifyingPluginProvider(classModifyingPlugins, undefined);

        assertThat(modifyingPluginProvider.classModifyingPlugins(), is(classModifyingPlugins));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnListOfTypeModifyingPlugins() throws Exception {
        final List<TypeModifyingPlugin> typeModifyingPlugins = mock(List.class);
        final List<ClassModifyingPlugin> undefined = null;

        final ModifyingPluginProvider modifyingPluginProvider = new ModifyingPluginProvider(undefined, typeModifyingPlugins);

        assertThat(modifyingPluginProvider.typeModifyingPlugins(), is(typeModifyingPlugins));
    }
}

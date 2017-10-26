package uk.gov.justice.generation.pojo.plugin;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;

import java.util.List;

import org.junit.Test;

public class ModifyingPluginProviderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldConstructModifyingPluginProvider() throws Exception {
        final List<TypeModifyingPlugin> typeModifyingPlugins = mock(List.class);
        final List<ClassModifyingPlugin> classModifyingPlugins = mock(List.class);

        final ModifyingPluginProvider modifyingPluginProvider = new ModifyingPluginProvider(classModifyingPlugins, typeModifyingPlugins);

        assertThat(modifyingPluginProvider.classModifyingPlugins(), is(classModifyingPlugins));
        assertThat(modifyingPluginProvider.typeModifyingPlugins(), is(typeModifyingPlugins));
    }
}

package uk.gov.justice.generation.pojo.plugin;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.ReferenceCustomReturnTypePlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.SupportJavaOptionalsPlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.custom.CustomReturnTypeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

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

    @Test
    public void shouldAddOptionalsPluginLast() throws Exception {
        final SupportJavaOptionalsPlugin supportJavaOptionalsPlugin = new SupportJavaOptionalsPlugin();
        final ReferenceCustomReturnTypePlugin referenceCustomReturnTypePlugin = new ReferenceCustomReturnTypePlugin(mock(CustomReturnTypeMapper.class), mock(Predicate.class));

        final List<TypeModifyingPlugin> typeModifyingPlugins = new ArrayList<>();
        typeModifyingPlugins.add(supportJavaOptionalsPlugin);
        typeModifyingPlugins.add(referenceCustomReturnTypePlugin);

        final List<ClassModifyingPlugin> classModifyingPlugins = mock(List.class);

        final ModifyingPluginProvider modifyingPluginProvider = new ModifyingPluginProvider(classModifyingPlugins, typeModifyingPlugins);

        assertThat(modifyingPluginProvider.typeModifyingPlugins().get(1), is(supportJavaOptionalsPlugin));
    }
}

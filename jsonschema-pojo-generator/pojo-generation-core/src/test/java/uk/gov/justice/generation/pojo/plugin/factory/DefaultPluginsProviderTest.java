package uk.gov.justice.generation.pojo.plugin.factory;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.plugin.classmodifying.AddFieldsAndMethodsToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.GenerateBuilderForClassPlugin;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPluginsProviderTest {

    @InjectMocks
    private DefaultPluginsProvider defaultPluginsProvider;

    @Test
    public void shouldGetTheListOfDefaultPlugins() throws Exception {

        final List<ClassModifyingPlugin> defaultPlugins = defaultPluginsProvider.getDefaultPlugins();

        assertThat(defaultPlugins.size(), is(2));
        assertThat(defaultPlugins.get(0), is(instanceOf(AddFieldsAndMethodsToClassPlugin.class)));
        assertThat(defaultPlugins.get(1), is(instanceOf(GenerateBuilderForClassPlugin.class)));
    }
}

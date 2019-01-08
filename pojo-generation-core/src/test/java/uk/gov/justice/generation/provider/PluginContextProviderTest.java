package uk.gov.justice.generation.provider;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;

import java.util.List;

import org.junit.Test;

public class PluginContextProviderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProvidePluginContext() throws Exception {
        final long serialVersionUID = 23L;
        final JavaGeneratorFactory javaGeneratorFactory = mock(JavaGeneratorFactory.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final String sourceFilename = "sourceFilename";
        final List<ClassModifyingPlugin> classModifyingPlugins = mock(List.class);
        final PojoGeneratorProperties generatorProperties = mock(PojoGeneratorProperties.class);

        final PluginContext pluginContext = new PluginContextProvider()
                .create(
                        javaGeneratorFactory,
                        classNameFactory,
                        sourceFilename,
                        classModifyingPlugins,
                        generatorProperties,
                        serialVersionUID);

        assertThat(getValueOfField(pluginContext, "generatorFactory", JavaGeneratorFactory.class), is(javaGeneratorFactory));
        assertThat(getValueOfField(pluginContext, "sourceFilename", String.class), is(sourceFilename));
        assertThat(getValueOfField(pluginContext, "classModifyingPlugins", List.class), is(classModifyingPlugins));
        assertThat(getValueOfField(pluginContext, "generatorProperties", PojoGeneratorProperties.class), is(generatorProperties));
        assertThat(getValueOfField(pluginContext, "serialVersionUID", Long.class), is(serialVersionUID));
    }
}

package uk.gov.justice.generation.provider;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static uk.gov.justice.generation.utils.ReflectionUtil.fieldValue;

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
                        generatorProperties
                );

        assertThat(fieldValue(pluginContext, "generatorFactory"), is(javaGeneratorFactory));
        assertThat(fieldValue(pluginContext, "sourceFilename"), is(sourceFilename));
        assertThat(fieldValue(pluginContext, "classModifyingPlugins"), is(classModifyingPlugins));
        assertThat(fieldValue(pluginContext, "generatorProperties"), is(generatorProperties));
    }
}

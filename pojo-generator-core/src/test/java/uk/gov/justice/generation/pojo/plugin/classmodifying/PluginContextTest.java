package uk.gov.justice.generation.pojo.plugin.classmodifying;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.classmodifying.properties.AdditionalPropertiesDeterminer;

import java.util.List;

import org.junit.Test;

public class PluginContextTest {

    private static final JavaGeneratorFactory UNSPECIFIED_GENERATOR_FACTORY = null;
    private static final ClassNameFactory UNSPECIFIED_CLASS_NAME_FACTORY = null;
    private static final String BLANK = "";

    @Test
    public void shouldReturnJavaGeneratorFactory() throws Exception {
        final JavaGeneratorFactory generatorFactory = mock(JavaGeneratorFactory.class);

        final PluginContext pluginContext = new PluginContext(
                generatorFactory,
                UNSPECIFIED_CLASS_NAME_FACTORY,
                BLANK,
                emptyList());

        assertThat(pluginContext.getJavaGeneratorFactory(), is(generatorFactory));
    }

    @Test
    public void shouldReturnClassNameFactory() throws Exception {
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);

        final PluginContext pluginContext = new PluginContext(
                UNSPECIFIED_GENERATOR_FACTORY,
                classNameFactory,
                BLANK,
                emptyList());

        assertThat(pluginContext.getClassNameFactory(), is(classNameFactory));
    }

    @Test
    public void shouldReturnSourceFilename() throws Exception {
        final String sourceFilename = "sourceFilename";

        final PluginContext pluginContext = new PluginContext(
                UNSPECIFIED_GENERATOR_FACTORY,
                UNSPECIFIED_CLASS_NAME_FACTORY,
                sourceFilename,
                emptyList());

        assertThat(pluginContext.getSourceFilename(), is(sourceFilename));
    }

    @Test
    public void shouldReturnTrueIfTheSpecifiedPluginIsInUseInTheApplication() throws Exception {

        final List<ClassModifyingPlugin> classModifyingPlugins = asList(
                new AddToStringMethodToClassPlugin(new AdditionalPropertiesDeterminer()),
                new MakeClassSerializablePlugin()
        );

        final PluginContext pluginContext = new PluginContext(
                UNSPECIFIED_GENERATOR_FACTORY,
                UNSPECIFIED_CLASS_NAME_FACTORY,
                BLANK,
                classModifyingPlugins);

        assertThat(pluginContext.isPluginInUse(AddToStringMethodToClassPlugin.class), is(true));
        assertThat(pluginContext.isPluginInUse(MakeClassSerializablePlugin.class), is(true));
        assertThat(pluginContext.isPluginInUse(AddAdditionalPropertiesToClassPlugin.class), is(false));
    }
}

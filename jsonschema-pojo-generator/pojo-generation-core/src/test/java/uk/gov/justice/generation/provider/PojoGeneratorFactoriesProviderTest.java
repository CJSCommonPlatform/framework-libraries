package uk.gov.justice.generation.provider;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.TypeNameProvider;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.plugin.TypeNamePluginProcessor;

import org.junit.Test;

public class PojoGeneratorFactoriesProviderTest {

    @Test
    public void shouldProvideJavaGeneratorFactory() throws Exception {
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);

        final JavaGeneratorFactory javaGeneratorFactory = new PojoGeneratorFactoriesProvider().createJavaGeneratorFactory(classNameFactory);

        assertThat(getValueOfField(javaGeneratorFactory, "classNameFactory", ClassNameFactory.class), is(classNameFactory));
    }

    @Test
    public void shouldProvideClassNameFactory() throws Exception {
        final GenerationContext generationContext = mock(GenerationContext.class);
        final PluginProvider pluginProvider = mock(PluginProvider.class);

        final ClassNameFactory classNameFactory = new PojoGeneratorFactoriesProvider().createClassNameFactory(generationContext, pluginProvider);

        assertThat(classNameFactory, notNullValue());

        final TypeNameProvider typeNameProvider = getValueOfField(classNameFactory, "typeNameProvider", TypeNameProvider.class);
        assertThat(getValueOfField(typeNameProvider, "generationContext", GenerationContext.class), is(generationContext));

        final TypeNamePluginProcessor typeNamePluginProcessor = getValueOfField(classNameFactory, "typeNamePluginProcessor", TypeNamePluginProcessor.class);
        assertThat(getValueOfField(typeNamePluginProcessor, "pluginProvider", PluginProvider.class), is(pluginProvider));
    }
}

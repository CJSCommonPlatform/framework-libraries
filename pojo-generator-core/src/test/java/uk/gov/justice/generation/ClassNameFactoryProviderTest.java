package uk.gov.justice.generation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static uk.gov.justice.generation.utils.ReflectionUtil.fieldValue;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;

import org.junit.Test;

public class ClassNameFactoryProviderTest {

    @Test
    public void shouldProvideClassNameFactory() throws Exception {
        final GenerationContext generationContext = mock(GenerationContext.class);
        final PluginProvider pluginProvider = mock(PluginProvider.class);

        final ClassNameFactory classNameFactory = new ClassNameFactoryProvider().create(generationContext, pluginProvider);

        assertThat(classNameFactory, notNullValue());

        final Object typeNameProvider = fieldValue(classNameFactory, "typeNameProvider");
        assertThat(fieldValue(typeNameProvider, "generationContext"), is(generationContext));

        final Object typeNamePluginProcessor = fieldValue(classNameFactory, "typeNamePluginProcessor");
        assertThat(fieldValue(typeNamePluginProcessor, "pluginProvider"), is(pluginProvider));
    }
}

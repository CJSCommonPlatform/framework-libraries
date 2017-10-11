package uk.gov.justice.generation.provider;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.fieldValue;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;

import java.util.Optional;

import org.junit.Test;

public class ClassNameFactoryProviderTest {

    @Test
    public void shouldProvideClassNameFactory() throws Exception {
        final GenerationContext generationContext = mock(GenerationContext.class);
        final PluginProvider pluginProvider = mock(PluginProvider.class);

        final ClassNameFactory classNameFactory = new ClassNameFactoryProvider().create(generationContext, pluginProvider);

        assertThat(classNameFactory, notNullValue());

        final Optional<Object> typeNameProvider = fieldValue(classNameFactory, "typeNameProvider");
        assertThat(typeNameProvider.isPresent(), is(true));
        assertThat(fieldValue(typeNameProvider.get(), "generationContext"), is(Optional.of(generationContext)));

        final Optional<Object> typeNamePluginProcessor = fieldValue(classNameFactory, "typeNamePluginProcessor");
        assertThat(typeNamePluginProcessor.isPresent(), is(true));
        assertThat(fieldValue(typeNamePluginProcessor.get(), "pluginProvider"), is(Optional.of(pluginProvider)));
    }
}

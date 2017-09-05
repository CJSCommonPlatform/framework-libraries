package uk.gov.justice.generation.pojo.generators;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.plugin.PluginClassGeneratable;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;

import com.squareup.javapoet.TypeSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassGeneratorTest {

    @Mock
    private ClassDefinition classDefinition;

    @Mock
    private JavaGeneratorFactory javaGeneratorFactory;

    @Mock
    private PluginProvider pluginProvider;

    @Mock
    private ClassNameFactory classNameFactory;

    @Mock
    private GenerationContext generationContext;

    @InjectMocks
    private ClassGenerator classGenerator;

    @Test
    public void shouldCaplitalizeTheFieldNameToUseAsTheClassName() throws Exception {

        when(pluginProvider.pluginClassGenerators()).thenReturn(emptyList());
        when(classDefinition.getFieldName()).thenReturn("alcubierreDrive");

        classGenerator.generate();

        assertThat(classGenerator.getSimpleClassName(), is("AlcubierreDrive"));
    }

    @Test
    public void shouldGenerateAnEmptyClassAndUseThePluginsToGenerateTheClassInternals() throws Exception {

        final PluginClassGeneratable plugin_1 = mock(PluginClassGeneratable.class, "plugin_1");
        final PluginClassGeneratable plugin_2 = mock(PluginClassGeneratable.class, "plugin_2");
        final PluginClassGeneratable plugin_3 = mock(PluginClassGeneratable.class, "plugin_3");

        when(pluginProvider.pluginClassGenerators()).thenReturn(asList(plugin_1, plugin_2, plugin_3));
        when(classDefinition.getFieldName()).thenReturn("alcubierreDrive");

        final TypeSpec classSpec = classGenerator.generate();

        assertThat(classSpec.toString(), is("public class AlcubierreDrive {\n}\n"));

        verify(plugin_1).generateWith(
                any(TypeSpec.Builder.class),
                eq(classDefinition),
                eq(javaGeneratorFactory),
                eq(classNameFactory),
                eq(generationContext));
        verify(plugin_2).generateWith(
                any(TypeSpec.Builder.class),
                eq(classDefinition),
                eq(javaGeneratorFactory),
                eq(classNameFactory),
                eq(generationContext));
        verify(plugin_3).generateWith(
                any(TypeSpec.Builder.class),
                eq(classDefinition),
                eq(javaGeneratorFactory),
                eq(classNameFactory),
                eq(generationContext));
    }
}

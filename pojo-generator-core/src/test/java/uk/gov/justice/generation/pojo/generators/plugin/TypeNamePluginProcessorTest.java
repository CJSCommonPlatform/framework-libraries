package uk.gov.justice.generation.pojo.generators.plugin;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeNamePlugin;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TypeNamePluginProcessorTest {

    @Mock
    private PluginProvider pluginProvider;

    @InjectMocks
    private TypeNamePluginProcessor typeNamePluginProcessor;

    @Test
    public void shouldProcessEachPluginInOrder() throws Exception {

        final TypeName originalTypeName = ClassName.get(String.class);
        final TypeName decoratedTypeName_1 = ClassName.get(Integer.class);
        final TypeName decoratedTypeName_2 = ClassName.get(Boolean.class);

        final TypeNamePlugin typeNamePlugin_1 = mock(TypeNamePlugin.class);
        final TypeNamePlugin typeNamePlugin_2 = mock(TypeNamePlugin.class);


        final Definition definition = mock(Definition.class);

        when(pluginProvider.typeNamePlugins()).thenReturn(asList(typeNamePlugin_1, typeNamePlugin_2));
        when(typeNamePlugin_1.modifyTypeName(originalTypeName, definition)).thenReturn(decoratedTypeName_1);
        when(typeNamePlugin_2.modifyTypeName(decoratedTypeName_1, definition)).thenReturn(decoratedTypeName_2);

        final TypeName finalTypeName = typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, definition);

        assertThat(finalTypeName, is(decoratedTypeName_2));
    }
}

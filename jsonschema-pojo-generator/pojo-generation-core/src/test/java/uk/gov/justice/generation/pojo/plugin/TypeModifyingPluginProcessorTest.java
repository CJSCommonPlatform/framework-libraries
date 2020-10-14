package uk.gov.justice.generation.pojo.plugin;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TypeModifyingPluginProcessorTest {

    @Mock
    private PluginProvider pluginProvider;

    @InjectMocks
    private TypeNamePluginProcessor typeNamePluginProcessor;

    @Test
    public void shouldProcessEachPluginInOrder() throws Exception {

        final TypeName originalTypeName = ClassName.get(String.class);
        final TypeName decoratedTypeName_1 = ClassName.get(Integer.class);
        final TypeName decoratedTypeName_2 = ClassName.get(Boolean.class);

        final TypeModifyingPlugin typeModifyingPlugin_1 = mock(TypeModifyingPlugin.class);
        final TypeModifyingPlugin typeModifyingPlugin_2 = mock(TypeModifyingPlugin.class);
        final PluginContext pluginContext = mock(PluginContext.class);


        final Definition definition = mock(Definition.class);

        when(pluginProvider.typeModifyingPlugins()).thenReturn(asList(typeModifyingPlugin_1, typeModifyingPlugin_2));
        when(typeModifyingPlugin_1.modifyTypeName(originalTypeName, definition, pluginContext)).thenReturn(decoratedTypeName_1);
        when(typeModifyingPlugin_2.modifyTypeName(decoratedTypeName_1, definition, pluginContext)).thenReturn(decoratedTypeName_2);

        final TypeName finalTypeName = typeNamePluginProcessor.processTypeNamePlugins(
                originalTypeName,
                definition,
                pluginContext);

        assertThat(finalTypeName, is(decoratedTypeName_2));
    }
}

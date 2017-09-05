package uk.gov.justice.generation.pojo.generators.plugin;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ARRAY;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.dom.Definition;

import java.util.List;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class OptionalTypeNamePluginTest {

    @InjectMocks
    private OptionalTypeNamePlugin optionalTypeNamePlugin;

    @Test
    public void shouldWrapTypeWithJavaOptionalIfFieldIsNotRequired() throws Exception {

        final TypeName originalTypeName = ClassName.get(String.class);

        final Definition definition = mock(Definition.class);

        when(definition.isRequired()).thenReturn(false);
        when(definition.type()).thenReturn(STRING);

        final TypeName typeName = optionalTypeNamePlugin.modifyTypeName(originalTypeName, definition);

        assertThat(typeName.toString(), is("java.util.Optional<java.lang.String>"));
    }

    @Test
    public void shouldNotWrapTypeWithJavaOptionalIfFieldIsRequired() throws Exception {

        final TypeName originalTypeName = ClassName.get(String.class);

        final Definition definition = mock(Definition.class);

        when(definition.isRequired()).thenReturn(true);
        when(definition.type()).thenReturn(STRING);

        final TypeName typeName = optionalTypeNamePlugin.modifyTypeName(originalTypeName, definition);

        assertThat(typeName.toString(), is("java.lang.String"));
    }

    @Test
    public void shouldNotWrapTypeWithJavaOptionalIfClassIsAnArray() throws Exception {

        final TypeName originalTypeName = ClassName.get(List.class);

        final Definition definition = mock(Definition.class);

        when(definition.isRequired()).thenReturn(false);
        when(definition.type()).thenReturn(ARRAY);

        final TypeName typeName = optionalTypeNamePlugin.modifyTypeName(originalTypeName, definition);

        assertThat(typeName.toString(), is("java.util.List"));
    }

    @Test
    public void shouldNotWrapTypeWithJavaOptionalIfClassIsAnArrayEvenIfTheFiledIsRequired() throws Exception {

        final TypeName originalTypeName = ClassName.get(List.class);

        final Definition definition = mock(Definition.class);

        when(definition.isRequired()).thenReturn(true);
        when(definition.type()).thenReturn(ARRAY);

        final TypeName typeName = optionalTypeNamePlugin.modifyTypeName(originalTypeName, definition);

        assertThat(typeName.toString(), is("java.util.List"));
    }
}

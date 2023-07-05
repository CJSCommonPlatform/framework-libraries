package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static com.squareup.javapoet.TypeName.get;
import static javax.lang.model.element.Modifier.PRIVATE;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import java.util.Optional;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FieldSpecFactoryTest {

    @Mock
    private OptionalTypeNameUtil optionalTypeNameUtil;

    @InjectMocks
    private FieldSpecFactory fieldSpecFactory;

    @Test
    public void shouldReturnFieldSpecForNonParameterizedType() {

        final Definition fieldDefinition = mock(Definition.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);
        final TypeName typeName = get(String.class);

        when(classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext)).thenReturn(typeName);
        when(optionalTypeNameUtil.isOptionalType(typeName)).thenReturn(false);
        when(fieldDefinition.getFieldName()).thenReturn("fieldDefinition");

        final FieldSpec resultFieldSpec = fieldSpecFactory.createFieldSpecFor(fieldDefinition, classNameFactory, pluginContext);

        assertThat(resultFieldSpec.name, is("fieldDefinition"));
        assertThat(resultFieldSpec.modifiers.size(), is(1));
        assertThat(resultFieldSpec.modifiers, hasItem(PRIVATE));
        assertThat(resultFieldSpec.type.toString(), is("java.lang.String"));
    }

    @Test
    public void shouldReturnFieldSpecOfParameterForOptionalParameterizedType() {

        final Definition fieldDefinition = mock(Definition.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);
        final ParameterizedTypeName optionalWithStringTypeName = ParameterizedTypeName.get(ClassName.get(Optional.class), ClassName.get(String.class));

        when(classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext)).thenReturn(optionalWithStringTypeName);
        when(optionalTypeNameUtil.isOptionalType(optionalWithStringTypeName)).thenReturn(true);
        when(optionalTypeNameUtil.getOptionalTypeFrom(optionalWithStringTypeName)).thenReturn(ClassName.get(String.class));
        when(fieldDefinition.getFieldName()).thenReturn("fieldDefinition");

        final FieldSpec resultFieldSpec = fieldSpecFactory.createFieldSpecFor(fieldDefinition, classNameFactory, pluginContext);

        assertThat(resultFieldSpec.name, is("fieldDefinition"));
        assertThat(resultFieldSpec.modifiers.size(), is(1));
        assertThat(resultFieldSpec.modifiers, hasItem(PRIVATE));
        assertThat(resultFieldSpec.type.toString(), is("java.lang.String"));
    }
}
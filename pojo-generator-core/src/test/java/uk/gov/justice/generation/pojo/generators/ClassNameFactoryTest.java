package uk.gov.justice.generation.pojo.generators;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ARRAY;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.BOOLEAN;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.COMBINED;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.INTEGER;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.NUMBER;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.plugin.TypeNamePluginProcessor;

import com.squareup.javapoet.TypeName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassNameFactoryTest {

    @Mock
    private TypeNameProvider typeNameProvider;

    @Mock
    private TypeNamePluginProcessor typeNamePluginProcessor;

    @InjectMocks
    private ClassNameFactory classNameFactory;

    @Test
    public void shouldCreateTheCorrectTypeNameForArrays() throws Exception {

        final Definition definition = mock(Definition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(definition.type()).thenReturn(ARRAY);
        when(typeNameProvider.typeNameForArray(definition, classNameFactory)).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, definition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(definition), is(modifiedTypeName));
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForBooleans() throws Exception {

        final Definition definition = mock(Definition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(definition.type()).thenReturn(BOOLEAN);
        when(typeNameProvider.typeNameForBoolean()).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, definition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(definition), is(modifiedTypeName));
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForIntegers() throws Exception {

        final Definition definition = mock(Definition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(definition.type()).thenReturn(INTEGER);
        when(typeNameProvider.typeNameForInteger()).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, definition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(definition), is(modifiedTypeName));
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForNumbers() throws Exception {

        final Definition definition = mock(Definition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(definition.type()).thenReturn(NUMBER);
        when(typeNameProvider.typeNameForNumber()).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, definition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(definition), is(modifiedTypeName));
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForStrings() throws Exception {

        final Definition definition = mock(Definition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(definition.type()).thenReturn(STRING);
        when(typeNameProvider.typeNameForString(definition)).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, definition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(definition), is(modifiedTypeName));
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForClasses() throws Exception {

        final Definition definition = mock(Definition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(definition.type()).thenReturn(CLASS);
        when(typeNameProvider.typeNameForClass(definition)).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, definition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(definition), is(modifiedTypeName));
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForEnums() throws Exception {

        final Definition definition = mock(Definition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(definition.type()).thenReturn(CLASS);
        when(typeNameProvider.typeNameForClass(definition)).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, definition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(definition), is(modifiedTypeName));
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForCombinedSchemas() throws Exception {

        final Definition definition = mock(Definition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(definition.type()).thenReturn(COMBINED);
        when(typeNameProvider.typeNameForClass(definition)).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, definition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(definition), is(modifiedTypeName));
    }
}

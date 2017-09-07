package uk.gov.justice.generation.pojo.generators;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ARRAY;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.BOOLEAN;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.COMBINED;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.INTEGER;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.NUMBER;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ROOT;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.CombinedDefinition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;
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

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(classDefinition.type()).thenReturn(ARRAY);
        when(typeNameProvider.typeNameForArray(classDefinition, classNameFactory)).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, classDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(classDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForArray(classDefinition, classNameFactory);
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalTypeName, classDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForBooleans() throws Exception {

        final FieldDefinition fieldDefinition = mock(FieldDefinition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(fieldDefinition.type()).thenReturn(BOOLEAN);
        when(typeNameProvider.typeNameForBoolean()).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, fieldDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(fieldDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForBoolean();
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalTypeName, fieldDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForIntegers() throws Exception {

        final FieldDefinition fieldDefinition = mock(FieldDefinition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(fieldDefinition.type()).thenReturn(INTEGER);
        when(typeNameProvider.typeNameForInteger()).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, fieldDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(fieldDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForInteger();
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalTypeName, fieldDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForNumbers() throws Exception {

        final FieldDefinition fieldDefinition = mock(FieldDefinition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(fieldDefinition.type()).thenReturn(NUMBER);
        when(typeNameProvider.typeNameForNumber()).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, fieldDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(fieldDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForNumber();
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalTypeName, fieldDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForStrings() throws Exception {

        final FieldDefinition stringDefinition = mock(FieldDefinition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(stringDefinition.type()).thenReturn(STRING);
        when(typeNameProvider.typeNameForString()).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, stringDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(stringDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForString();
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalTypeName, stringDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForClasses() throws Exception {

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(classDefinition.type()).thenReturn(CLASS);
        when(typeNameProvider.typeNameForClass(classDefinition)).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, classDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(classDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForClass(classDefinition);
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalTypeName, classDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForEnums() throws Exception {

        final EnumDefinition enumDefinition = mock(EnumDefinition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(enumDefinition.type()).thenReturn(CLASS);
        when(typeNameProvider.typeNameForClass(enumDefinition)).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, enumDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(enumDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForClass(enumDefinition);
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalTypeName, enumDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForCombinedSchemas() throws Exception {

        final CombinedDefinition combinedDefinition = mock(CombinedDefinition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(combinedDefinition.type()).thenReturn(COMBINED);
        when(typeNameProvider.typeNameForClass(combinedDefinition)).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, combinedDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(combinedDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForClass(combinedDefinition);
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalTypeName, combinedDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForRootSchemas() throws Exception {

        final ClassDefinition rootDefinition = mock(ClassDefinition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(rootDefinition.type()).thenReturn(ROOT);
        when(typeNameProvider.typeNameForClass(rootDefinition)).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, rootDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(rootDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForClass(rootDefinition);
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalTypeName, rootDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForReference() throws Exception {

        final ReferenceDefinition referenceDefinition = mock(ReferenceDefinition.class);
        final TypeName originalTypeName = mock(TypeName.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(referenceDefinition.type()).thenReturn(REFERENCE);
        when(typeNameProvider.typeNameForReference(referenceDefinition, classNameFactory)).thenReturn(originalTypeName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalTypeName, referenceDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(referenceDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForReference(referenceDefinition, classNameFactory);
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalTypeName, referenceDefinition);
    }
}

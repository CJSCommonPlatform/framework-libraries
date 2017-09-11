package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.ClassName.get;
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

import java.math.BigDecimal;

import com.squareup.javapoet.ClassName;
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
    public void shouldCreateTheCorrectClassNameForBooleans() throws Exception {

        final FieldDefinition fieldDefinition = mock(FieldDefinition.class);
        final ClassName originalClassName = get(Boolean.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(fieldDefinition.type()).thenReturn(BOOLEAN);
        when(typeNameProvider.typeNameForBoolean()).thenReturn(originalClassName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalClassName, fieldDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(fieldDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForBoolean();
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalClassName, fieldDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForIntegers() throws Exception {

        final FieldDefinition fieldDefinition = mock(FieldDefinition.class);
        final ClassName originalClassName = get(Integer.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(fieldDefinition.type()).thenReturn(INTEGER);
        when(typeNameProvider.typeNameForInteger()).thenReturn(originalClassName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalClassName, fieldDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(fieldDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForInteger();
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalClassName, fieldDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForNumbers() throws Exception {

        final FieldDefinition fieldDefinition = mock(FieldDefinition.class);
        final ClassName originalClassName = get(BigDecimal.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(fieldDefinition.type()).thenReturn(NUMBER);
        when(typeNameProvider.typeNameForNumber()).thenReturn(originalClassName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalClassName, fieldDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(fieldDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForNumber();
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalClassName, fieldDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForStrings() throws Exception {

        final FieldDefinition stringDefinition = mock(FieldDefinition.class);
        final ClassName originalClassName = get(String.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(stringDefinition.type()).thenReturn(STRING);
        when(typeNameProvider.typeNameForString()).thenReturn(originalClassName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalClassName, stringDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(stringDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForString();
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalClassName, stringDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForClasses() throws Exception {

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final ClassName originalClassName = get(TestClass.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(classDefinition.type()).thenReturn(CLASS);
        when(typeNameProvider.typeNameForClass(classDefinition)).thenReturn(originalClassName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalClassName, classDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(classDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForClass(classDefinition);
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalClassName, classDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForEnums() throws Exception {

        final EnumDefinition enumDefinition = mock(EnumDefinition.class);
        final ClassName originalClassName = get(Enum.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(enumDefinition.type()).thenReturn(CLASS);
        when(typeNameProvider.typeNameForClass(enumDefinition)).thenReturn(originalClassName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalClassName, enumDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(enumDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForClass(enumDefinition);
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalClassName, enumDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForCombinedSchemas() throws Exception {

        final CombinedDefinition combinedDefinition = mock(CombinedDefinition.class);
        final ClassName originalClassName = get(TestClass.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(combinedDefinition.type()).thenReturn(COMBINED);
        when(typeNameProvider.typeNameForClass(combinedDefinition)).thenReturn(originalClassName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalClassName, combinedDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(combinedDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForClass(combinedDefinition);
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalClassName, combinedDefinition);
    }

    @Test
    public void shouldCreateTheCorrectTypeNameForRootSchemas() throws Exception {

        final ClassDefinition rootDefinition = mock(ClassDefinition.class);
        final ClassName originalClassName = get(TestClass.class);
        final TypeName modifiedTypeName = mock(TypeName.class);

        when(rootDefinition.type()).thenReturn(ROOT);
        when(typeNameProvider.typeNameForClass(rootDefinition)).thenReturn(originalClassName);
        when(typeNamePluginProcessor.processTypeNamePlugins(originalClassName, rootDefinition)).thenReturn(modifiedTypeName);

        assertThat(classNameFactory.createTypeNameFrom(rootDefinition), is(modifiedTypeName));

        verify(typeNameProvider).typeNameForClass(rootDefinition);
        verify(typeNamePluginProcessor).processTypeNamePlugins(originalClassName, rootDefinition);
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

    @Test
    public void shouldCreateTheCorrectClassNameForClasses() throws Exception {

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final ClassName className = get(TestClass.class);

        when(typeNameProvider.typeNameForClass(classDefinition)).thenReturn(className);

        assertThat(classNameFactory.createClassNameFrom(classDefinition), is(className));

        verify(typeNameProvider).typeNameForClass(classDefinition);
    }

    private class TestClass {
    }
}

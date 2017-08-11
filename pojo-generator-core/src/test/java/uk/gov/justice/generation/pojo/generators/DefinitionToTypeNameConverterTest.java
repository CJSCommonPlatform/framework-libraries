package uk.gov.justice.generation.pojo.generators;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;

import java.util.List;

import com.squareup.javapoet.TypeName;
import org.junit.Test;

public class DefinitionToTypeNameConverterTest {

    private final DefinitionToTypeNameConverter definitionToTypeNameConverter = new DefinitionToTypeNameConverter();

    @Test
    public void shouldGetAClassNameWithNoGenericTypeIfDefinitionHasNoGenericType() throws Exception {

        final Definition definition = new FieldDefinition("fieldName", new ClassName(String.class));

        final TypeName typeName = definitionToTypeNameConverter.getTypeName(definition);

        assertThat(typeName, is(instanceOf(com.squareup.javapoet.ClassName.class)));
        assertThat(typeName.toString(), is("java.lang.String"));
    }

    @Test
    public void shouldGetAClassNameWithGenericTypeIfDefinitionHasGenericType() throws Exception {

        final Definition definition = new FieldDefinition("fieldName", new ClassName(List.class), new ClassName(String.class));

        final TypeName typeName = definitionToTypeNameConverter.getTypeName(definition);

        assertThat(typeName, is(instanceOf(com.squareup.javapoet.TypeName.class)));
        assertThat(typeName.toString(), is("java.util.List<java.lang.String>"));
    }
}

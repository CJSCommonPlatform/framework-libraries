package uk.gov.justice.generation.pojo.generators;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import com.squareup.javapoet.TypeName;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;

import java.util.List;

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

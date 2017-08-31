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

public class ClassNameFactoryTest {

    private final ClassNameFactory classNameFactory = new ClassNameFactory();

    @Test
    public void shouldGetAClassNameWithNoGenericTypeIfDefinitionHasNoGenericType() throws Exception {

        final Definition definition = new FieldDefinition("fieldName", new ClassName(String.class));

        final TypeName typeName = classNameFactory.createClassNameFrom(definition);

        assertThat(typeName, is(instanceOf(com.squareup.javapoet.ClassName.class)));
        assertThat(typeName.toString(), is("java.lang.String"));
    }

    @Test
    public void shouldGetAClassNameWithGenericTypeIfDefinitionHasGenericType() throws Exception {

        final Definition definition = new FieldDefinition("fieldName", new ClassName(List.class), new ClassName(String.class));

        final TypeName typeName = classNameFactory.createClassNameFrom(definition);

        assertThat(typeName, is(instanceOf(com.squareup.javapoet.TypeName.class)));
        assertThat(typeName.toString(), is("java.util.List<java.lang.String>"));
    }

    @Test
    public void shouldGetAClassNameThatIsAJavaOptional() throws Exception {

        final Definition definition = new FieldDefinition("fieldName", new ClassName(String.class));
        definition.setRequired(false);

        final TypeName typeName = classNameFactory.createClassNameFrom(definition);

        assertThat(typeName, is(instanceOf(com.squareup.javapoet.TypeName.class)));
        assertThat(typeName.toString(), is("java.util.Optional<java.lang.String>"));
    }
}

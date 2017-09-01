package uk.gov.justice.generation.pojo.generators;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ARRAY;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.BOOLEAN;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.INTEGER;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.NUMBER;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.dom.StringDefinition;

import com.squareup.javapoet.TypeName;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ClassNameFactoryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private final ClassNameFactory classNameFactory = new ClassNameFactory("package.name");

    @Test
    public void shouldReturnClassNameForStringDefinitionWithUnknownDescription() throws Exception {

        final Definition definition = new StringDefinition("fieldName", "description");

        final TypeName typeName = classNameFactory.createClassNameFrom(definition);

        assertThat(typeName, is(instanceOf(com.squareup.javapoet.ClassName.class)));
        assertThat(typeName.toString(), is("java.lang.String"));
    }

    @Test
    public void shouldReturnClassNameForStringDefinitionWithUuidDescription() throws Exception {

        final Definition definition = new StringDefinition("fieldName", "UUID");

        final TypeName typeName = classNameFactory.createClassNameFrom(definition);

        assertThat(typeName, is(instanceOf(com.squareup.javapoet.ClassName.class)));
        assertThat(typeName.toString(), is("java.util.UUID"));
    }

    @Test
    public void shouldReturnClassNameForStringDefinitionWithZoneDateTimeDescription() throws Exception {

        final Definition definition = new StringDefinition("fieldName", "ZonedDateTime");

        final TypeName typeName = classNameFactory.createClassNameFrom(definition);

        assertThat(typeName, is(instanceOf(com.squareup.javapoet.ClassName.class)));
        assertThat(typeName.toString(), is("java.time.ZonedDateTime"));
    }

    @Test
    public void shouldReturnClassNameForFieldDefinitionOfTypeInteger() throws Exception {

        final Definition definition = new FieldDefinition(INTEGER, "fieldName");

        final TypeName typeName = classNameFactory.createClassNameFrom(definition);

        assertThat(typeName, is(instanceOf(com.squareup.javapoet.ClassName.class)));
        assertThat(typeName.toString(), is("java.lang.Integer"));
    }

    @Test
    public void shouldReturnClassNameForFieldDefinitionOfTypeNumber() throws Exception {

        final Definition definition = new FieldDefinition(NUMBER, "fieldName");

        final TypeName typeName = classNameFactory.createClassNameFrom(definition);

        assertThat(typeName, is(instanceOf(com.squareup.javapoet.ClassName.class)));
        assertThat(typeName.toString(), is("java.math.BigDecimal"));
    }

    @Test
    public void shouldReturnClassNameForFieldDefinitionOfTypeBoolean() throws Exception {

        final Definition definition = new FieldDefinition(BOOLEAN, "fieldName");

        final TypeName typeName = classNameFactory.createClassNameFrom(definition);

        assertThat(typeName, is(instanceOf(com.squareup.javapoet.ClassName.class)));
        assertThat(typeName.toString(), is("java.lang.Boolean"));
    }

    @Test
    public void shouldReturnClassNameForArrayDefinitionWithStringDefinition() throws Exception {

        final ClassDefinition definition = new ClassDefinition(ARRAY, "fieldName");
        definition.addFieldDefinition(new StringDefinition("fieldName", "description"));

        final TypeName typeName = classNameFactory.createClassNameFrom(definition);

        assertThat(typeName, is(instanceOf(com.squareup.javapoet.TypeName.class)));
        assertThat(typeName.toString(), is("java.util.List<java.lang.String>"));
    }

    @Test
    public void shouldReturnClassNameForArrayDefinitionWithClassDefinition() throws Exception {

        final ClassDefinition definition = new ClassDefinition(ARRAY, "fieldName");
        definition.addFieldDefinition(new ClassDefinition(CLASS, "innerField"));

        final TypeName typeName = classNameFactory.createClassNameFrom(definition);

        assertThat(typeName, is(instanceOf(com.squareup.javapoet.TypeName.class)));
        assertThat(typeName.toString(), is("java.util.List<package.name.InnerField>"));
    }

    @Test
    public void shouldGetAClassNameThatIsAJavaOptional() throws Exception {

        final Definition definition = new StringDefinition("fieldName", "description");
        definition.setRequired(false);

        final TypeName typeName = classNameFactory.createClassNameFrom(definition);

        assertThat(typeName, is(instanceOf(com.squareup.javapoet.TypeName.class)));
        assertThat(typeName.toString(), is("java.util.Optional<java.lang.String>"));
    }

    @Test
    public void shouldThrowExceptionIfArrayDefinitionHasNoChildDefintions() throws Exception {

        final ClassDefinition definition = new ClassDefinition(ARRAY, "fieldName");

        expectedException.expect(GenerationException.class);
        expectedException.expectMessage("No definition present for array types. For field: fieldName");

        classNameFactory.createClassNameFrom(definition);
    }
}

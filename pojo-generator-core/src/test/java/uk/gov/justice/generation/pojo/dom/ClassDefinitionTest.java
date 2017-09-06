package uk.gov.justice.generation.pojo.dom;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;

import java.util.List;

import org.junit.Test;

public class ClassDefinitionTest {

    @Test
    public void shouldReturnFieldNameUsedInConstruction() throws Exception {
        final String fieldName = "test";
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, fieldName);

        assertThat(classDefinition, is(instanceOf(FieldDefinition.class)));
        assertThat(classDefinition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldAddAndRetrieveFieldDefinitions() throws Exception {
        final FieldDefinition fieldDefinition1 = mock(FieldDefinition.class);
        final FieldDefinition fieldDefinition2 = mock(FieldDefinition.class);
        final FieldDefinition fieldDefinition3 = mock(FieldDefinition.class);

        when(fieldDefinition1.getFieldName()).thenReturn("fieldDefinition1");
        when(fieldDefinition2.getFieldName()).thenReturn("fieldDefinition2");
        when(fieldDefinition3.getFieldName()).thenReturn("fieldDefinition3");

        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "fieldName");
        classDefinition.addFieldDefinition(fieldDefinition1);
        classDefinition.addFieldDefinition(fieldDefinition2);
        classDefinition.addFieldDefinition(fieldDefinition3);

        final List<Definition> fieldDefinitions = classDefinition.getFieldDefinitions();
        assertThat(fieldDefinitions.size(), is(3));
        assertThat(fieldDefinitions, hasItems(fieldDefinition1, fieldDefinition2, fieldDefinition3));
    }

    @Test
    public void shouldGetFieldDefinitionsSortedByFieldName() throws Exception {

        final FieldDefinition fieldDefinition_1 = mock(FieldDefinition.class);
        final FieldDefinition fieldDefinition_2 = mock(FieldDefinition.class);
        final FieldDefinition fieldDefinition_3 = mock(FieldDefinition.class);

        when(fieldDefinition_1.getFieldName()).thenReturn("zzz");
        when(fieldDefinition_2.getFieldName()).thenReturn("aaa");
        when(fieldDefinition_3.getFieldName()).thenReturn("mmm");

        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "fieldName");
        classDefinition.addFieldDefinition(fieldDefinition_1);
        classDefinition.addFieldDefinition(fieldDefinition_2);
        classDefinition.addFieldDefinition(fieldDefinition_3);

        final List<Definition> fieldDefinitions = classDefinition.getFieldDefinitions();

        assertThat(fieldDefinitions.get(0), is(fieldDefinition_2));
        assertThat(fieldDefinitions.get(1), is(fieldDefinition_3));
        assertThat(fieldDefinitions.get(2), is(fieldDefinition_1));
    }

    @Test
    public void additionalPropertiesShouldBeFalseByDefault() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "myField");
        assertThat(classDefinition.allowAdditionalProperties(), is(false));
    }

    @Test
    public void shouldAllowAdditionalProperties() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "fieldName");

        assertThat(classDefinition.allowAdditionalProperties(), is(false));

        classDefinition.setAllowAdditionalProperties(true);

        assertThat(classDefinition.allowAdditionalProperties(), is(true));
    }
}

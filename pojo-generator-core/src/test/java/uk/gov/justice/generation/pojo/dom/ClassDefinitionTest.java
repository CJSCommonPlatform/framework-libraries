package uk.gov.justice.generation.pojo.dom;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class ClassDefinitionTest {

    @Test
    public void shouldReturnClassNameUsedInConstruction() throws Exception {
        final ClassName className = mock(ClassName.class);
        final ClassDefinition classDefinition = new ClassDefinition("", className);
        assertThat(classDefinition.getClassName(), is(className));
    }

    @Test
    public void shouldReturnFiledNameUsedInConstruction() throws Exception {
        final String fieldName = "test";
        final ClassDefinition classDefinition = new ClassDefinition(fieldName, null);
        assertThat(classDefinition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldAddAndRetrieveFieldDefintions() throws Exception {
        final FieldDefinition fieldDefinition1 = mock(FieldDefinition.class);
        final FieldDefinition fieldDefinition2 = mock(FieldDefinition.class);
        final FieldDefinition fieldDefinition3 = mock(FieldDefinition.class);

        final ClassDefinition classDefinition = new ClassDefinition("", mock(ClassName.class));
        classDefinition.addFieldDefinition(fieldDefinition1);
        classDefinition.addFieldDefinition(fieldDefinition2);
        classDefinition.addFieldDefinition(fieldDefinition3);

        final List<Definition> fieldDefinitions = classDefinition.getFieldDefinitions();
        assertThat(fieldDefinitions.size(), is(3));
        assertThat(fieldDefinitions, CoreMatchers.hasItems(fieldDefinition1, fieldDefinition2, fieldDefinition3));
    }
}

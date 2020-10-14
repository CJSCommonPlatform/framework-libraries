package uk.gov.justice.generation.pojo.dom;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

public class ClassDefinitionTest {

    @Test
    public void shouldReturnFieldNameUsedInConstruction() throws Exception {
        final String fieldName = "test";
        final String id = "http://id.com/id";
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, fieldName, id);

        assertThat(classDefinition, is(instanceOf(FieldDefinition.class)));
        assertThat(classDefinition.getFieldName(), is(fieldName));
        assertThat(classDefinition.getId(), is(Optional.of(id)));
    }

    @Test
    public void shouldAddAndRetrieveFieldDefinitions() throws Exception {
        final FieldDefinition fieldDefinition1 = mock(FieldDefinition.class);
        final FieldDefinition fieldDefinition2 = mock(FieldDefinition.class);
        final FieldDefinition fieldDefinition3 = mock(FieldDefinition.class);

        when(fieldDefinition1.getFieldName()).thenReturn("fieldDefinition1");
        when(fieldDefinition2.getFieldName()).thenReturn("fieldDefinition2");
        when(fieldDefinition3.getFieldName()).thenReturn("fieldDefinition3");

        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "fieldName", "id");
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

        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "fieldName", "id");
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
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "myField", "id");
        assertThat(classDefinition.allowAdditionalProperties(), is(false));
    }

    @Test
    public void shouldAllowAdditionalProperties() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "fieldName", "id");

        assertThat(classDefinition.allowAdditionalProperties(), is(false));

        classDefinition.setAllowAdditionalProperties(true);

        assertThat(classDefinition.allowAdditionalProperties(), is(true));
    }

    @Test
    public void shouldBeTheRootClass() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "test", "id");

        classDefinition.setRoot(true);

        assertThat(classDefinition, is(instanceOf(FieldDefinition.class)));
        assertThat(classDefinition.isRoot(), is(true));
    }

    @Test
    public void shouldNotBeTheRootClass() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "test", "id");

        classDefinition.setRoot(false);

        assertThat(classDefinition, is(instanceOf(FieldDefinition.class)));
        assertThat(classDefinition.isRoot(), is(false));
    }

    @Test
    public void shouldDefaultToNotBeTheRootClass() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "test", "id");

        assertThat(classDefinition, is(instanceOf(FieldDefinition.class)));
        assertThat(classDefinition.isRoot(), is(false));
    }

    @Test
    public void shouldTestToStringMethodToStopCoverallsWhining() throws Exception {

        final String fieldName = "test";
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, fieldName, "http://id.com/id");

        assertThat(classDefinition.toString(), is("ClassDefinition{fieldName=test, id=Optional[http://id.com/id], type=CLASS, required=true, allowAdditionalProperties=false, root=false}"));
    }
}

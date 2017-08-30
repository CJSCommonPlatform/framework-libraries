package uk.gov.justice.generation.pojo.dom;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

public class CombinedDefinitionTest {

    @Test
    public void shouldConstructCombinedDefinitionWithNoEventName() throws Exception {
        final String fieldName = "fieldName";
        final ClassName className = mock(ClassName.class);
        final CombinedDefinition combinedDefinition = new CombinedDefinition(fieldName, className);

        assertThat(combinedDefinition.getFieldName(), is(fieldName));
        assertThat(combinedDefinition.getClassName(), is(className));
        assertThat(combinedDefinition.getEventName(), is(Optional.empty()));
        assertThat(combinedDefinition.getFieldDefinitions().size(), is(0));
    }

    @Test
    public void shouldConstructCombinedDefinitionWithEventName() throws Exception {
        final String fieldName = "fieldName";
        final ClassName className = mock(ClassName.class);
        final String eventName = "eventName";
        final CombinedDefinition combinedDefinition = new CombinedDefinition(fieldName, className, eventName);

        assertThat(combinedDefinition.getFieldName(), is(fieldName));
        assertThat(combinedDefinition.getClassName(), is(className));
        assertThat(combinedDefinition.getEventName(), is(Optional.of(eventName)));
        assertThat(combinedDefinition.getFieldDefinitions().size(), is(0));
    }

    @Test
    public void shouldReturnListOfFieldDefintionsFromChildDefinitions() throws Exception {
        final String combinedFieldName = "outerCombinedFieldName";
        final String outerClassFieldName = "outerClassFieldName";
        final String innerClassFieldName = "innerClassFieldName";
        final String stringField_1 = "stringFieldName_1";
        final String stringField_2 = "stringFieldName_2";
        final String stringField_3 = "stringFieldName_2";
        final ClassName className = mock(ClassName.class);
        final String eventName = "eventName";

        final CombinedDefinition outerCombinedDefinition = new CombinedDefinition(combinedFieldName, className, eventName);
        final CombinedDefinition innerCombinedDefinition = new CombinedDefinition(combinedFieldName, className);
        final ClassDefinition outerClassDefinition = new ClassDefinition(outerClassFieldName, className);
        final ClassDefinition innerClassDefinition = new ClassDefinition(innerClassFieldName, className);
        final FieldDefinition fieldDefinition_1 = new FieldDefinition(stringField_1, className);
        final FieldDefinition fieldDefinition_2 = new FieldDefinition(stringField_2, className);
        final FieldDefinition fieldDefinition_3 = new FieldDefinition(stringField_3, className);

        outerClassDefinition.addFieldDefinition(fieldDefinition_1);
        innerClassDefinition.addFieldDefinition(fieldDefinition_2);
        innerCombinedDefinition.addFieldDefinition(innerClassDefinition);
        outerCombinedDefinition.addFieldDefinition(fieldDefinition_3);
        outerCombinedDefinition.addFieldDefinition(innerCombinedDefinition);
        outerCombinedDefinition.addFieldDefinition(outerClassDefinition);

        final List<Definition> fieldDefinitions = outerCombinedDefinition.getFieldDefinitions();

        assertThat(fieldDefinitions.size(), is(3));
        assertThat(fieldDefinitions.get(0).getFieldName(), is(stringField_3));
        assertThat(fieldDefinitions.get(1).getFieldName(), is(stringField_2));
        assertThat(fieldDefinitions.get(2).getFieldName(), is(stringField_1));
    }
}
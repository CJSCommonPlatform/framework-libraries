package uk.gov.justice.generation.pojo.dom;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import java.util.List;

import org.junit.Test;

public class CombinedDefinitionTest {

    @Test
    public void shouldConstructCombinedDefinition() throws Exception {
        final String fieldName = "fieldName";
        final CombinedDefinition combinedDefinition = new CombinedDefinition(fieldName);

        assertThat(combinedDefinition, is(instanceOf(ClassDefinition.class)));
        assertThat(combinedDefinition.getFieldName(), is(fieldName));
        assertThat(combinedDefinition.getFieldDefinitions().size(), is(0));
    }

    @Test
    public void shouldReturnListOfFieldDefinitionsFromChildDefinitions() throws Exception {
        final String combinedFieldName = "outerCombinedFieldName";
        final String outerClassFieldName = "outerClassFieldName";
        final String innerClassFieldName = "innerClassFieldName";
        final String stringField_1 = "stringFieldName_1";
        final String stringField_2 = "stringFieldName_2";
        final String stringField_3 = "stringFieldName_2";

        final CombinedDefinition outerCombinedDefinition = new CombinedDefinition(combinedFieldName);
        final CombinedDefinition innerCombinedDefinition = new CombinedDefinition(combinedFieldName);
        final ClassDefinition outerClassDefinition = new ClassDefinition(CLASS, outerClassFieldName);
        final ClassDefinition innerClassDefinition = new ClassDefinition(CLASS, innerClassFieldName);
        final FieldDefinition fieldDefinition_1 = new FieldDefinition(STRING, stringField_1);
        final FieldDefinition fieldDefinition_2 = new FieldDefinition(STRING, stringField_2);
        final FieldDefinition fieldDefinition_3 = new FieldDefinition(STRING, stringField_3);

        outerClassDefinition.addFieldDefinition(fieldDefinition_1);
        innerClassDefinition.addFieldDefinition(fieldDefinition_2);
        innerCombinedDefinition.addFieldDefinition(innerClassDefinition);
        outerCombinedDefinition.addFieldDefinition(fieldDefinition_3);
        outerCombinedDefinition.addFieldDefinition(innerCombinedDefinition);
        outerCombinedDefinition.addFieldDefinition(outerClassDefinition);

        final List<Definition> fieldDefinitions = outerCombinedDefinition.getFieldDefinitions();

        assertThat(fieldDefinitions.size(), is(3));
        assertThat(fieldDefinitions.get(0).getFieldName(), is(stringField_1));
        assertThat(fieldDefinitions.get(1).getFieldName(), is(stringField_2));
        assertThat(fieldDefinitions.get(2).getFieldName(), is(stringField_3));
    }
}

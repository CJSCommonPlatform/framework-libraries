package uk.gov.justice.generation.pojo.dom;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.INTEGER;

import org.junit.Test;

public class FieldDefinitionTest {

    @Test
    public void shouldCreateFieldDefinition() throws Exception {
        final DefinitionType definitionType = INTEGER;
        final String fieldName = "fieldName";

        final FieldDefinition fieldDefinition = new FieldDefinition(definitionType, fieldName);

        assertThat(fieldDefinition, is(instanceOf(Definition.class)));
        assertThat(fieldDefinition.type(), is(definitionType));
        assertThat(fieldDefinition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldAllowSettingOfRequired() throws Exception {
        final String fieldName = "fieldName";

        final FieldDefinition fieldDefinition = new FieldDefinition(INTEGER, fieldName);

        assertThat(fieldDefinition.isRequired(), is(true));

        fieldDefinition.setRequired(false);

        assertThat(fieldDefinition.isRequired(), is(false));
    }
}

package uk.gov.justice.generation.pojo.dom;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import org.junit.Test;

public class StringDefinitionTest {

    @Test
    public void shouldCreateDefinitionOfTypeString() throws Exception {
        final String fieldName = "fieldName";
        final String description = "description";

        final StringDefinition stringDefinition = new StringDefinition(fieldName, description);

        assertThat(stringDefinition, is(instanceOf(FieldDefinition.class)));
        assertThat(stringDefinition.type(), is(STRING));
        assertThat(stringDefinition.getFieldName(), is(fieldName));
        assertThat(stringDefinition.getDescription(), is(description));
    }
}

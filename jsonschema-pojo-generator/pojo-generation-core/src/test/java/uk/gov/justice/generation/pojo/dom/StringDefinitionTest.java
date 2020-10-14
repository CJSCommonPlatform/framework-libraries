package uk.gov.justice.generation.pojo.dom;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import java.util.Optional;

import org.junit.Test;

public class StringDefinitionTest {

    @Test
    public void shouldCreateStringDefinitionWithFormat() throws Exception {
        final String fieldName = "fieldName";
        final String format = "date-time";

        final StringDefinition stringDefinition = new StringDefinition(fieldName, format);

        assertThat(stringDefinition, is(instanceOf(FieldDefinition.class)));
        assertThat(stringDefinition.type(), is(STRING));
        assertThat(stringDefinition.getFieldName(), is(fieldName));
        assertThat(stringDefinition.getFormat(), is(Optional.of(format)));
    }

    @Test
    public void shouldCreateStringDefinitionWithNoFormat() throws Exception {
        final String fieldName = "fieldName";
        final String format = "date-time";

        final StringDefinition stringDefinition = new StringDefinition(fieldName);

        assertThat(stringDefinition, is(instanceOf(FieldDefinition.class)));
        assertThat(stringDefinition.type(), is(STRING));
        assertThat(stringDefinition.getFieldName(), is(fieldName));
        assertThat(stringDefinition.getFormat(), is(Optional.empty()));
    }
}

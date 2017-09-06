package uk.gov.justice.generation.pojo.dom;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

public class EnumDefinitionTest {

    @Test
    public void shouldConstructEnumDefinition() throws Exception {
        final String fieldName = "fieldName";
        final List<String> enumValues = emptyList();

        final EnumDefinition enumDefinition = new EnumDefinition(fieldName, enumValues);

        assertThat(enumDefinition, is(instanceOf(FieldDefinition.class)));
        assertThat(enumDefinition.getFieldName(), is(fieldName));
        assertThat(enumDefinition.getEnumValues(), is(enumValues));
    }
}
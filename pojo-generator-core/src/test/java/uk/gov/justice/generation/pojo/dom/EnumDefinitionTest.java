package uk.gov.justice.generation.pojo.dom;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Test;

public class EnumDefinitionTest {

    @Test
    public void shouldConstructEnumDefinition() throws Exception {
        final String fieldName = "fieldName";
        final ClassName className = mock(ClassName.class);
        final List<String> enumValues = emptyList();

        final EnumDefinition enumDefinition = new EnumDefinition(fieldName, className, enumValues);

        assertThat(enumDefinition.getFieldName(), is(fieldName));
        assertThat(enumDefinition.getClassName(), is(className));
        assertThat(enumDefinition.getEnumValues(), is(enumValues));
    }
}
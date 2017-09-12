package uk.gov.justice.generation.pojo.generators.plugin.typename;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.junit.Test;

public class SupportZonedDateTimePluginTest {

    @Test
    public void shouldReturnOriginalTypeNameIfReferenceButNotZonedDateTime() throws Exception {
        final TypeName originalTypeName = ClassName.get(String.class);
        final ReferenceDefinition referenceDefinition = mock(ReferenceDefinition.class);

        when(referenceDefinition.type()).thenReturn(REFERENCE);
        when(referenceDefinition.getReferenceValue()).thenReturn("#/definitions/dateString");

        final TypeName resultTypeName = new SupportZonedDateTimePlugin().modifyTypeName(originalTypeName, referenceDefinition);

        assertThat(resultTypeName.toString(), is("java.lang.String"));
    }

    @Test
    public void shouldReturnUuidTypeNameIfReferenceAndIsZonedDateTime() throws Exception {
        final TypeName originalTypeName = ClassName.get(String.class);
        final ReferenceDefinition referenceDefinition = mock(ReferenceDefinition.class);

        when(referenceDefinition.type()).thenReturn(REFERENCE);
        when(referenceDefinition.getReferenceValue()).thenReturn("#/definitions/ZonedDateTime");

        final TypeName resultTypeName = new SupportZonedDateTimePlugin().modifyTypeName(originalTypeName, referenceDefinition);

        assertThat(resultTypeName.toString(), is("java.time.ZonedDateTime"));
    }

    @Test
    public void shouldReturnOriginalTypeNameIfNotReference() throws Exception {
        final TypeName originalTypeName = ClassName.get(String.class);
        final ReferenceDefinition referenceDefinition = mock(ReferenceDefinition.class);

        when(referenceDefinition.type()).thenReturn(STRING);

        final TypeName resultTypeName = new SupportZonedDateTimePlugin().modifyTypeName(originalTypeName, referenceDefinition);

        assertThat(resultTypeName.toString(), is("java.lang.String"));
    }

}

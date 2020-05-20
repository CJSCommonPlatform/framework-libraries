package uk.gov.justice.generation.pojo.dom;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.visitor.ReferenceValue;

import org.junit.Test;

public class ReferenceDefinitionTest {

    @Test
    public void shouldConstructReferenceDefinition() throws Exception {
        final String fieldName = "fieldName";
        final ReferenceValue referenceValue = ReferenceValue.fromReferenceValueString("reference/value");

        final ReferenceDefinition referenceDefinition = new ReferenceDefinition(fieldName, referenceValue);

        assertThat(referenceDefinition, is(instanceOf(ClassDefinition.class)));
        assertThat(referenceDefinition.getFieldName(), is(fieldName));
        assertThat(referenceDefinition.getReferenceValue(), is(referenceValue));
    }
}

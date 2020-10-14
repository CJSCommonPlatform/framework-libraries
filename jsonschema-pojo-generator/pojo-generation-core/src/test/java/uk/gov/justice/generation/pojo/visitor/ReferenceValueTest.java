package uk.gov.justice.generation.pojo.visitor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;


public class ReferenceValueTest {

    @Test
    public void shouldCreateAReferenceValueFromAReferenceValueString() throws Exception {

        final String referenceValueString = "#/definitions/uuid";

        final ReferenceValue referenceValue = ReferenceValue.fromReferenceValueString(referenceValueString);

        assertThat(referenceValue.getName(), is("uuid"));
        assertThat(referenceValue.getPath(), is("#/definitions"));

        assertThat(referenceValue.toString(),  is(referenceValueString));
    }
}

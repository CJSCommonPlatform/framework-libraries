package uk.gov.justice.generation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.events.pojo.PersonSchema;

import java.math.BigDecimal;

import org.junit.Test;

public class SchemaToJavaGeneratorPluginTest {

    @Test
    public void shouldCreateJavaPojosFromSchema() throws Exception {
        final String firstName = "firstName";
        final String lastName = "lastName";
        final boolean required = true;
        final int signedInCount = 10;
        final BigDecimal ratio = BigDecimal.valueOf(1.55);

        final PersonSchema personSchema = new PersonSchema(firstName, lastName, required, signedInCount, ratio);

        assertThat(personSchema.getFirstName(), is(firstName));
        assertThat(personSchema.getLastName(), is(lastName));
        assertThat(personSchema.getRequired(), is(required));
        assertThat(personSchema.getSignedInCount(), is(signedInCount));
        assertThat(personSchema.getRatio(), is(ratio));
    }
}
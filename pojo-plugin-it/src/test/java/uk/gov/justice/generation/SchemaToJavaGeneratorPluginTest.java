package uk.gov.justice.generation;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;

import uk.gov.justice.events.pojo.PersonSchema;
import uk.gov.justice.events.pojo.Title;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class SchemaToJavaGeneratorPluginTest {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

    @Test
    public void shouldCreateJavaPojosFromSchema() throws Exception {
        final Title title = Title.BLANK;
        final String firstName = "firstName";
        final String lastName = "lastName";
        final boolean required = true;
        final int signedInCount = 10;
        final BigDecimal ratio = BigDecimal.valueOf(1.55);

        final PersonSchema person = new PersonSchema(firstName, lastName, title, required, signedInCount, ratio);

        final String personJson = objectMapper.writeValueAsString(person);

        with(personJson)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.title", is(""))
                .assertThat("$.required", is(required))
                .assertThat("$.signedInCount", is(signedInCount))
                .assertThat("$.ratio", is(ratio.doubleValue()))
        ;
    }
}
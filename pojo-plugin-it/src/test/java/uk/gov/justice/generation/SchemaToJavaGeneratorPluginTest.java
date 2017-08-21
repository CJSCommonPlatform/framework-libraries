package uk.gov.justice.generation;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;

import uk.gov.justice.events.pojo.Alias;
import uk.gov.justice.events.pojo.PersonSchema;
import uk.gov.justice.events.pojo.Title;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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
        final List<Alias> aliases = Arrays.asList(
                new Alias("1", "forename_1", "surname_1"),
                new Alias("2", "forename_2", "surname_2"));

        final PersonSchema person = new PersonSchema(firstName, lastName, aliases, title, required, signedInCount, ratio);

        final String personJson = objectMapper.writeValueAsString(person);

        with(personJson)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.aliasList[0].aliasInitials", is("1"))
                .assertThat("$.aliasList[0].aliasForenames", is("forename_1"))
                .assertThat("$.aliasList[0].aliasSurname", is("surname_1"))
                .assertThat("$.aliasList[1].aliasInitials", is("2"))
                .assertThat("$.aliasList[1].aliasForenames", is("forename_2"))
                .assertThat("$.aliasList[1].aliasSurname", is("surname_2"))
                .assertThat("$.title", is(""))
                .assertThat("$.required", is(required))
                .assertThat("$.signedInCount", is(signedInCount))
                .assertThat("$.ratio", is(ratio.doubleValue()))
        ;
    }
}
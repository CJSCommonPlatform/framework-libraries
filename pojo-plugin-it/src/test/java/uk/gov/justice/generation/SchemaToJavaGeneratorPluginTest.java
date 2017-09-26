package uk.gov.justice.generation;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.of;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.events.pojo.Alias;
import uk.gov.justice.events.pojo.PersonUpdated;
import uk.gov.justice.events.pojo.Title;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
                new Alias("forename_1", "1", "surname_1"),
                new Alias("forename_2", "2", "surname_2"));

        final UUID personId = randomUUID();
        final ZonedDateTime startDate = of(2017, 9, 28, 17, 0, 0, 0, UTC);

        final PersonUpdated person = new PersonUpdated(
                aliases,
                firstName,
                lastName,
                personId,
                ratio,
                required,
                signedInCount,
                startDate,
                title);

        final String personJson = objectMapper.writeValueAsString(person);

        with(personJson)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.personId", is(personId.toString()))
                .assertThat("$.title", is(""))
                .assertThat("$.required", is(required))
                .assertThat("$.signedInCount", is(signedInCount))
                .assertThat("$.startDate", is("2017-09-28T17:00:00.000Z"))
                .assertThat("$.ratio", is(ratio.doubleValue()))
                .assertThat("$.alias[0].aliasInitials", is("1"))
                .assertThat("$.alias[0].aliasForenames", is("forename_1"))
                .assertThat("$.alias[0].aliasSurname", is("surname_1"))
                .assertThat("$.alias[1].aliasInitials", is("2"))
                .assertThat("$.alias[1].aliasForenames", is("forename_2"))
                .assertThat("$.alias[1].aliasSurname", is("surname_2"))
        ;
    }

    @Test
    public void shouldCreateSerializablePojosFromSchema() throws Exception {
        final Title title = Title.MR;
        final String firstName = "firstName";
        final String lastName = "lastName";
        final boolean required = true;
        final int signedInCount = 10;
        final BigDecimal ratio = BigDecimal.valueOf(1.55);
        final List<Alias> aliases = Arrays.asList(
                new Alias("forename_1", "1", "surname_1"),
                new Alias("forename_2", "2", "surname_2"));

        final UUID personId = randomUUID();
        final ZonedDateTime startDate = of(2017, 9, 28, 17, 0, 0, 0, UTC);

        final PersonUpdated person = new PersonUpdated(
                aliases,
                firstName,
                lastName,
                personId,
                ratio,
                required,
                signedInCount,
                startDate,
                title);

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(person);

        final ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        final PersonUpdated resultObject = (PersonUpdated) objectInputStream.readObject();

        assertThat(resultObject.getTitle(), is(Title.MR));
        assertThat(resultObject.getFirstName(), is(firstName));
        assertThat(resultObject.getLastName(), is(lastName));
        assertThat(resultObject.getRequired(), is(required));
        assertThat(resultObject.getSignedInCount(), is(signedInCount));
        assertThat(resultObject.getRatio(), is(ratio));
        assertThat(resultObject.getAlias().size(), is(2));

        assertThat(resultObject.getAlias().get(0).getAliasInitials(), is("1"));
        assertThat(resultObject.getAlias().get(0).getAliasForenames(), is("forename_1"));
        assertThat(resultObject.getAlias().get(0).getAliasSurname(), is("surname_1"));

        assertThat(resultObject.getAlias().get(1).getAliasInitials(), is("2"));
        assertThat(resultObject.getAlias().get(1).getAliasForenames(), is("forename_2"));
        assertThat(resultObject.getAlias().get(1).getAliasSurname(), is("surname_2"));
    }
}

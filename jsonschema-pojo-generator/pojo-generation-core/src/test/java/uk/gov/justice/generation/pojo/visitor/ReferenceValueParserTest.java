package uk.gov.justice.generation.pojo.visitor;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ReferenceValueParserTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldParseReferenceValueFromValidJsonString() throws Exception {

        final String definitionString = "#/definitions/uuid";
        final String jsonValue = "{\n\"$ref\": \"" + definitionString + "\"\n}";

        final ReferenceValue result = new ReferenceValueParser().parseFrom(new StringReader(jsonValue), "fieldName");

        assertThat(result.getPath(), is("#/definitions"));
        assertThat(result.getName(), is("uuid"));
        assertThat(result.toString(), is(definitionString));
    }

    @Test
    public void shouldCatchAndThrowExceptionIfIOExceptionIsThrown() throws Exception {
        expectedException.expect(FailedToParseSchemaException.class);
        expectedException.expectMessage(is("Failed to parse ReferenceSchema $ref value for field name: fieldName"));
        expectedException.expectCause(instanceOf(IOException.class));

        final Reader reader = mock(Reader.class);

        doThrow(new IOException()).when(reader);

        new ReferenceValueParser().parseFrom(reader, "fieldName");
    }
}

package uk.gov.justice.generation.pojo.visitor;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public class ReferenceValueParserTest {

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

        final Reader reader = mock(Reader.class);

        doThrow(new IOException()).when(reader);

        final FailedToParseSchemaException failedToParseSchemaException = assertThrows(FailedToParseSchemaException.class, () ->
                new ReferenceValueParser().parseFrom(reader, "fieldName")
        );

        assertThat(failedToParseSchemaException.getMessage(), is("Failed to parse ReferenceSchema $ref value for field name: fieldName"));
        assertThat(failedToParseSchemaException.getCause(), is(instanceOf(IOException.class)));
    }
}

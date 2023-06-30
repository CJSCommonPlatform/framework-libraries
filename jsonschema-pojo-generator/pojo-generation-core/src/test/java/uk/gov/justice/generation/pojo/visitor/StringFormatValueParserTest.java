package uk.gov.justice.generation.pojo.visitor;

import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class StringFormatValueParserTest {

    private final StringFormatValueParser stringFormatValueParser = new StringFormatValueParser();

    @Test
    public void shouldParseFormatValueFromValidJsonString() throws Exception {
        final String formatValue = "date-time";
        final String jsonValue = "{\n\"format\": \"" + formatValue + "\"\n}";

        final Optional<String> result = stringFormatValueParser.parseFrom(new StringReader(jsonValue), "fieldName");

        assertThat(result, is(of(formatValue)));
    }

    @Test
    public void shouldReturnOptionalEmptyIfNoFormat() throws Exception {
        final String jsonValue = "{\n\"name\": \"value\"\n}";

        final Optional<String> result = stringFormatValueParser.parseFrom(new StringReader(jsonValue), "fieldName");

        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void shouldCatchAndThrowExceptionIfIOExceptionIsThrown() throws Exception {

        final Reader reader = mock(Reader.class);

        doThrow(new IOException()).when(reader);

        final FailedToParseSchemaException failedToParseSchemaException = assertThrows(FailedToParseSchemaException.class, () ->
                stringFormatValueParser.parseFrom(reader, "fieldName")
        );

        assertThat(failedToParseSchemaException.getMessage(), is("Failed to parse StringSchema format value for field name: fieldName"));
        assertThat(failedToParseSchemaException.getCause(), is(instanceOf(IOException.class)));
    }
}

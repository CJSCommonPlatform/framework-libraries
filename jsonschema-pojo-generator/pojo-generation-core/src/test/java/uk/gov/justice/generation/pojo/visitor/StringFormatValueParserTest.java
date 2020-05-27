package uk.gov.justice.generation.pojo.visitor;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StringFormatValueParserTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private StringFormatValueParser stringFormatValueParser;

    @Test
    public void shouldParseFormatValueFromValidJsonString() throws Exception {
        final String formatValue = "date-time";
        final String jsonValue = "{\n\"format\": \"" + formatValue + "\"\n}";

        final Optional<String> result = stringFormatValueParser.parseFrom(new StringReader(jsonValue), "fieldName");

        assertThat(result, is(Optional.of(formatValue)));
    }

    @Test
    public void shouldReturnOptionalEmptyIfNoFormat() throws Exception {
        final String jsonValue = "{\n\"name\": \"value\"\n}";

        final Optional<String> result = stringFormatValueParser.parseFrom(new StringReader(jsonValue), "fieldName");

        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void shouldCatchAndThrowExceptionIfIOExceptionIsThrown() throws Exception {
        expectedException.expect(FailedToParseSchemaException.class);
        expectedException.expectMessage(is("Failed to parse StringSchema format value for field name: fieldName"));
        expectedException.expectCause(instanceOf(IOException.class));

        final Reader reader = mock(Reader.class);

        doThrow(new IOException()).when(reader);

        stringFormatValueParser.parseFrom(reader, "fieldName");
    }
}

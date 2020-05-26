package uk.gov.justice.maven.generator.io.files.parser.generator;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.maven.generator.io.files.parser.FileParser;
import uk.gov.justice.maven.generator.io.files.parser.generator.parser.TestFileParser;
import uk.gov.justice.maven.generator.io.files.parser.generator.parser.TestFileParserFactory;

import org.junit.Test;

public class FileParserInstanceFactoryTest {

    @Test
    public void shouldCreateInstanceOfFileParserFromGivenFileParserName() throws Exception {

        final String parserName = TestFileParser.class.getName();

        final FileParser fileParser = new FileParserInstanceFactory().newInstanceOf(parserName);

        assertThat(fileParser, is(instanceOf(TestFileParser.class)));
    }

    @Test
    public void shouldCreateInstanceOfFileParserFromGivenParserFactoryName() throws Exception {

        final String parserFactoryName = TestFileParserFactory.class.getName();

        final FileParser fileParser = new FileParserInstanceFactory().newInstanceOf(parserFactoryName);

        assertThat(fileParser, is(instanceOf(TestFileParser.class)));
    }

    @Test
    public void shouldThrowExceptionIfUnableToInstantiateFileParser() throws Exception {

        final String parserName = "unknown";

        try {
            new FileParserInstanceFactory().newInstanceOf(parserName);
            fail();
        } catch (final FileParserInstantiationException ex) {
            assertThat(ex.getMessage(), is("Failed to instantiate file parser: unknown"));
            assertThat(ex.getCause(), is(instanceOf(ClassNotFoundException.class)));
        }
    }
}
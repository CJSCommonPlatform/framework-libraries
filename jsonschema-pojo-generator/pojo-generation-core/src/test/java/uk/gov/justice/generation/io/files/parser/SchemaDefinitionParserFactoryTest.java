package uk.gov.justice.generation.io.files.parser;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.maven.generator.io.files.parser.FileParser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchemaDefinitionParserFactoryTest {

    @InjectMocks
    private SchemaDefinitionParserFactory schemaDefinitionParserFactory;

    @Test
    public void shouldCreateANewSchemaDefinitionParser() throws Exception {

        final FileParser<SchemaDefinition> schemaDefinitionFileParser = schemaDefinitionParserFactory.create();

        assertThat(schemaDefinitionFileParser, is(notNullValue()));
        assertThat(schemaDefinitionFileParser, is(instanceOf(SchemaDefinitionParser.class)));
    }
}

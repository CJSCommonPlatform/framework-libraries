package uk.gov.justice.generation.io.files.parser;

import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class SchemaDefinitionParserTest {

    private final Path basePath = get("src/test/resources/parser/");

    @Test
    public void shouldReturnCollectionOfSchemaContainerForGivenPath() throws Exception {
        final List<Path> paths = asList(
                get("test1.json"),
                get("test2.json"));

        final Collection<SchemaDefinition> schemaDefinitions = new SchemaDefinitionParser().parse(basePath, paths);

        assertThat(schemaDefinitions.size(), is(2));
        final Iterator<SchemaDefinition> schemaContainerIterator = schemaDefinitions.iterator();
        final SchemaDefinition schemaDefinition_1 = schemaContainerIterator.next();
        assertThat(schemaDefinition_1.getSchema().getId(), is("http://justice.gov.uk/context/test1.json"));
        assertThat(schemaDefinition_1.getFilename(), is("test1.json"));

        final SchemaDefinition schemaDefinition_2 = schemaContainerIterator.next();
        assertThat(schemaDefinition_2.getSchema().getId(), is("http://justice.gov.uk/context/test2.json"));
        assertThat(schemaDefinition_2.getFilename(), is("test2.json"));
    }

    @Test
    public void shouldReturnColectionOfSchemaFromClassPath() {
        final Collection<SchemaDefinition> schemaDefinitions = new SchemaDefinitionParser().parse(
                get("CLASSPATH"),
                asList(
                        get("parser/test1.json"),
                        get("parser/test2.json")));

        assertThat(schemaDefinitions.size(), is(2));
        final Iterator<SchemaDefinition> schemaContainerIterator = schemaDefinitions.iterator();
        final SchemaDefinition schemaDefinition_1 = schemaContainerIterator.next();
        assertThat(schemaDefinition_1.getSchema().getId(), is("http://justice.gov.uk/context/test1.json"));
        assertThat(schemaDefinition_1.getFilename(), is("test1.json"));

        final SchemaDefinition schemaDefinition_2 = schemaContainerIterator.next();
        assertThat(schemaDefinition_2.getSchema().getId(), is("http://justice.gov.uk/context/test2.json"));
        assertThat(schemaDefinition_2.getFilename(), is("test2.json"));
    }

    @Test
    public void shouldOnlyReturnValidSchema() {
        final Collection<SchemaDefinition> schemaDefinitions = new SchemaDefinitionParser().parse(
                get("CLASSPATH"),
                asList(
                        get("parser/test1.json"),
                        get("parser/test1.txt"),
                        get("parser/test2.json")));

        assertThat(schemaDefinitions.size(), is(2));
        final Iterator<SchemaDefinition> schemaContainerIterator = schemaDefinitions.iterator();
        final SchemaDefinition schemaDefinition_1 = schemaContainerIterator.next();
        assertThat(schemaDefinition_1.getSchema().getId(), is("http://justice.gov.uk/context/test1.json"));
        assertThat(schemaDefinition_1.getFilename(), is("test1.json"));

        final SchemaDefinition schemaDefinition_2 = schemaContainerIterator.next();
        assertThat(schemaDefinition_2.getSchema().getId(), is("http://justice.gov.uk/context/test2.json"));
        assertThat(schemaDefinition_2.getFilename(), is("test2.json"));
    }

    @Test
    public void shouldReturnEmptyCollectionForNoPaths() throws Exception {
        final Collection<SchemaDefinition> schemaDefinitions = new SchemaDefinitionParser().parse(basePath, emptyList());

        assertThat(schemaDefinitions.isEmpty(), is(true));
    }
}
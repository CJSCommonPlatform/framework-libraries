package uk.gov.justice.schema.catalog.generation.io.parser;

import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URI;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class ListOfUriParserTest {

    private final Path basePath = get("src/test/resources/parser/");

    @Test
    public void shouldReturnCollectionOfFilesForGivenPath() throws Exception {
        final List<Path> paths = asList(
                get("test1.txt"),
                get("test2.txt"));

        final Collection<List<URI>> collection = new ListOfUriParser().parse(basePath, paths);

        assertThat(collection.size(), is(1));

        final List<URI> uris = collection.iterator().next();
        assertThat(uris.size(), is(2));
        assertThat(uris, hasItems(
                basePath.resolve("test1.txt").toAbsolutePath().toUri(),
                basePath.resolve("test2.txt").toAbsolutePath().toUri()));
    }

    @Test
    public void shouldReturnEmoptyCollectionForNoPaths() throws Exception {
        final Collection<List<URI>> collection = new ListOfUriParser().parse(basePath, emptyList());

        assertThat(collection.size(), is(1));

        final List<URI> uris = collection.iterator().next();
        assertThat(uris.size(), is(0));
    }
}
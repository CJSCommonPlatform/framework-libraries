package uk.gov.justice.generation.io.files.parser;

import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class FileTypeParserTest {

    private final Path basePath = get("src/test/resources/parser/");

    @Test
    public void shouldReturnCollectionOfFilesForGivenPath() throws Exception {
        final List<Path> paths = asList(
                get("test1.txt"),
                get("test2.txt"));

        final Collection<File> files = new FileTypeParser().parse(basePath, paths);

        assertThat(files.size(), is(2));
        assertThat(files, hasItems(
                basePath.resolve("test1.txt").toAbsolutePath().toFile(),
                basePath.resolve("test2.txt").toAbsolutePath().toFile()));
    }

    @Test
    public void shouldReturnEmoptyCollectionForNoPaths() throws Exception {
        final Collection<File> files = new FileTypeParser().parse(basePath, emptyList());

        assertThat(files.size(), is(0));
    }
}
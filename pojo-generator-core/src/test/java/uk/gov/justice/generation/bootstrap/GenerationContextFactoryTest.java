package uk.gov.justice.generation.bootstrap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.write.SourceCodeWriteException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@SuppressWarnings("ResultOfMethodCallIgnored")
@RunWith(MockitoJUnitRunner.class)
public class GenerationContextFactoryTest {

    private final Path deletableTestPath = Paths.get("target/for-testing-please-delete");

    @Before
    public void createTemporaryTestDirectory() throws Exception {
        final File directory = deletableTestPath.toFile();

        directory.delete();
        directory.mkdirs();
        directory.deleteOnExit();
        assertThat(directory.exists(), is(true));
        assertThat(directory.isDirectory(), is(true));
        assertThat(directory.canWrite(), is(true));
    }

    @After
    public void deleteTemporaryTestDirectory() throws Exception {

        deletableTestPath.toFile().delete();
    }

    @InjectMocks
    private GenerationContextFactory generationContextFactory;

    @Test
    public void shouldCreateAGenerationContext() throws Exception {

        final Path sourceRootPath = deletableTestPath.resolve("source-root-directory");
        sourceRootPath.toFile().createNewFile();

        final GenerationContext generationContext = generationContextFactory.createWith(sourceRootPath);

        assertThat(generationContext.getOutputDirectoryPath(), is(sourceRootPath));
    }

    @Test
    public void shouldFailIfTheSourceRootDirectoryDoesNotExist() throws Exception {
        final Path sourceRootPath = Paths.get("non-existent-directory");

        assertThat(sourceRootPath.toFile().exists(), is(false));

        try {
            generationContextFactory.createWith(sourceRootPath);
            fail();
        } catch (final SourceCodeWriteException expected) {
            assertThat(expected.getMessage(), is("Source code root directory '" + sourceRootPath.toAbsolutePath() + "' does not exist"));
        }
    }

    @Test
    public void shouldFailIfTheSourceRootDirectoryIsNotADirectory() throws Exception {
        final Path sourceRootPath = Paths.get(pathToThisFile());
        final File sourceRootDirectory = sourceRootPath.toFile();

        assertThat(sourceRootDirectory.exists(), is(true));
        assertThat(sourceRootDirectory.isDirectory(), is(false));

        try {
            generationContextFactory.createWith(sourceRootPath);
            fail();
        } catch (final SourceCodeWriteException expected) {
            assertThat(expected.getMessage(), is("Source code root directory '" + sourceRootPath.toAbsolutePath() + "' is not a directory"));
        }
    }

    @Test
    public void shouldFailIfTheSourceRootDirectoryIsNotWritable() throws Exception {

        final Path sourceRootPath = deletableTestPath.resolve("source-root-directory");
        final File sourceRootDirectory = sourceRootPath.toFile();

        sourceRootDirectory.mkdirs();

        assertThat(sourceRootDirectory.isDirectory(), is(true));

        try {
            sourceRootDirectory.setWritable(false);
            assertThat(sourceRootDirectory.canWrite(), is(false));

            try {
                generationContextFactory.createWith(sourceRootPath);
                fail();
            } catch (final SourceCodeWriteException expected) {
                assertThat(expected.getMessage(), is("Source code root directory '" + sourceRootPath.toAbsolutePath() + "' is not writable"));
            }

        } finally {
            sourceRootDirectory.setWritable(true);
            assertThat(sourceRootDirectory.canWrite(), is(true));
        }
    }

    private String pathToThisFile() {
        return "src/test/java/" + getClass().getName().replace('.', '/') + ".java";
    }
}

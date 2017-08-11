package uk.gov.justice.generation.bootstrap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.write.SourceCodeWriteException;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@SuppressWarnings("ResultOfMethodCallIgnored")
@RunWith(MockitoJUnitRunner.class)
public class GenerationContextFactoryTest {

    private final File deletableTestDirectory = new File("target/for-testing-please-delete");

    @Before
    public void createTemporaryTestDirectory() throws Exception {

        deletableTestDirectory.delete();
        deletableTestDirectory.mkdirs();
        deletableTestDirectory.deleteOnExit();
        assertThat(deletableTestDirectory.exists(), is(true));
        assertThat(deletableTestDirectory.isDirectory(), is(true));
        assertThat(deletableTestDirectory.canWrite(), is(true));
    }

    @After
    public void deleteTemporaryTestDirectory() throws Exception {

        deletableTestDirectory.delete();
    }

    @InjectMocks
    private GenerationContextFactory generationContextFactory;

    @Test
    public void shouldCreateAGenerationContext() throws Exception {

        final File sourceRootDirectory = new File(deletableTestDirectory, "source-root-directory");
        sourceRootDirectory.createNewFile();

        final GenerationContext generationContext = generationContextFactory.create(sourceRootDirectory);

        assertThat(generationContext.getSourceRootDirectory(), is(sourceRootDirectory));
    }

    @Test
    public void shouldFailIfTheSourceRootDirectoryDoesNotExist() throws Exception {
        final File sourceRootDirectory = new File("non-existent-directory");

        assertThat(sourceRootDirectory.exists(), is(false));

        try {
            generationContextFactory.create(sourceRootDirectory);
            fail();
        } catch (final SourceCodeWriteException expected) {
            assertThat(expected.getMessage(), is("Source code root directory '" + sourceRootDirectory.getAbsolutePath() + "' does not exist"));
        }
    }

    @Test
    public void shouldFailIfTheSourceRootDirectoryIsNotADirectory() throws Exception {
        final File sourceRootDirectory = new File(pathToThisFile());

        System.out.println(sourceRootDirectory.getAbsolutePath());

        assertThat(sourceRootDirectory.exists(), is(true));
        assertThat(sourceRootDirectory.isDirectory(), is(false));

        try {
            generationContextFactory.create(sourceRootDirectory);
            fail();
        } catch (final SourceCodeWriteException expected) {
            assertThat(expected.getMessage(), is("Source code root directory '" + sourceRootDirectory.getAbsolutePath() + "' is not a directory"));
        }
    }

    @Test
    public void shouldFailIfTheSourceRootDirectoryIsNotWritable() throws Exception {

        final File sourceRootDirectory = new File(deletableTestDirectory, "source-root-directory");
        sourceRootDirectory.mkdirs();

        assertThat(sourceRootDirectory.isDirectory(), is(true));

        try {
            sourceRootDirectory.setWritable(false);
            assertThat(sourceRootDirectory.canWrite(), is(false));

            try {
                generationContextFactory.create(sourceRootDirectory);
                fail();
            } catch (final SourceCodeWriteException expected) {
                assertThat(expected.getMessage(), is("Source code root directory '" + sourceRootDirectory.getAbsolutePath() + "' is not writable"));
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

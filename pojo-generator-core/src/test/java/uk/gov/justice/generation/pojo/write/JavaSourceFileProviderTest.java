package uk.gov.justice.generation.pojo.write;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.Definition;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JavaSourceFileProviderTest {

    @InjectMocks
    private JavaSourceFileProvider javaSourceFileProvider;

    @Test
    public void shouldReturnAnExistingJavaFile() throws Exception {

        final Path sourceRootDirectory = Paths.get("src/main/java");

        final Class<Definition> definitionClass = Definition.class;
        final GenerationContext generationContext = new GenerationContext(sourceRootDirectory, definitionClass.getPackage().getName());

        final File javaFile = javaSourceFileProvider.getJavaFile(generationContext, definitionClass.getSimpleName());

        assertThat(javaFile.exists(), is(true));

        assertThat(javaFile.getName(), is(definitionClass.getSimpleName() + ".java"));
    }

    @Test
    public void shouldReturnANonExistentJavaFile() throws Exception {

        final Path sourceRootDirectory = Paths.get("src/main/java");

        final Class<Definition> definitionClass = Definition.class;
        final GenerationContext generationContext = new GenerationContext(sourceRootDirectory, "NotAYetExistingClass");

        final File javaFile = javaSourceFileProvider.getJavaFile(generationContext, "NotAYetExistingClass");

        assertThat(javaFile.exists(), is(false));

        assertThat(javaFile.getName(), is("NotAYetExistingClass.java"));
    }
}

package uk.gov.justice.generation.pojo.write;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.dom.ClassName;
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
        final ClassName className = new ClassName(definitionClass.getPackage().getName(), definitionClass.getSimpleName());

        final File javaFile = javaSourceFileProvider.getJavaFile(sourceRootDirectory, className);

        assertThat(javaFile.exists(), is(true));

        assertThat(javaFile.getName(), is(className.getSimpleName() + ".java"));
    }

    @Test
    public void shouldReturnANonExistentJavaFile() throws Exception {

        final Path sourceRootDirectory = Paths.get("src/main/java");

        final Class<Definition> definitionClass = Definition.class;
        final ClassName className = new ClassName(definitionClass.getPackage().getName(), "NotAYetExistingClass");

        final File javaFile = javaSourceFileProvider.getJavaFile(sourceRootDirectory, className);

        assertThat(javaFile.exists(), is(false));

        assertThat(javaFile.getName(), is(className.getSimpleName() + ".java"));
    }
}

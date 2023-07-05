package uk.gov.justice.generation.pojo.write;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JavaSourceFileProviderTest {

    @InjectMocks
    private JavaSourceFileProvider javaSourceFileProvider;

    @Test
    public void shouldReturnAnExistingJavaFile() throws Exception {

        final URL resource = getClass().getResource("/");
        assertThat(resource, is(notNullValue()));
        final Path classesDir = Paths.get(resource.toURI());
        final Path parent = classesDir.getParent().getParent();

        final Path sourceRootDirectory = parent.resolve("src/main/java");
        final String sourceFilename = "filename";

        final Class<Definition> definitionClass = Definition.class;
        final GenerationContext generationContext = new GenerationContext(sourceRootDirectory, "", sourceFilename, emptyList());

        final ClassGeneratable classGeneratable = mock(ClassGeneratable.class);

        when(classGeneratable.getSimpleClassName()).thenReturn(definitionClass.getSimpleName());
        when(classGeneratable.getPackageName()).thenReturn(definitionClass.getPackage().getName());

        final File javaFile = javaSourceFileProvider.getJavaFile(generationContext, classGeneratable);

        assertThat(javaFile.exists(), is(true));

        assertThat(javaFile.getName(), is(definitionClass.getSimpleName() + ".java"));
    }

    @Test
    public void shouldReturnANonExistentJavaFile() throws Exception {

        final URL resource = getClass().getResource("/");
        assertThat(resource, is(notNullValue()));
        final Path classesDir = Paths.get(resource.toURI());
        final Path parent = classesDir.getParent().getParent();

        final Path sourceRootDirectory = parent.resolve("src/main/java");
        final String sourceFilename = "filename";

        final GenerationContext generationContext = new GenerationContext(sourceRootDirectory, "", sourceFilename, emptyList());

        final ClassGeneratable classGeneratable = mock(ClassGeneratable.class);

        when(classGeneratable.getSimpleClassName()).thenReturn("NotAYetExistingClass");
        when(classGeneratable.getPackageName()).thenReturn("NotAYetExistingClass");

        final File javaFile = javaSourceFileProvider.getJavaFile(generationContext, classGeneratable);

        assertThat(javaFile.exists(), is(false));

        assertThat(javaFile.getName(), is("NotAYetExistingClass.java"));
    }
}

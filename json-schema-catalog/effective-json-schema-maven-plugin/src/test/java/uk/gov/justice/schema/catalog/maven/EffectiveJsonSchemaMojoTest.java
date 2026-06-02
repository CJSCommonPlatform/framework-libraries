package uk.gov.justice.schema.catalog.maven;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.generation.effective.CatalogJsonSchemaLoader;
import uk.gov.justice.schema.catalog.generation.effective.EffectiveJsonSchemaGenerationException;
import uk.gov.justice.schema.catalog.generation.effective.EffectiveJsonSchemaGenerator;
import uk.gov.justice.schema.catalog.generation.effective.EffectiveJsonSchemaObjectFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EffectiveJsonSchemaMojoTest {

    @TempDir
    Path tempDir;

    @Mock
    private EffectiveJsonSchemaObjectFactory objectFactory;

    @Mock
    private MavenProject project;

    @Mock
    private CatalogJsonSchemaLoader catalogJsonSchemaLoader;

    @Mock
    private EffectiveJsonSchemaGenerator effectiveJsonSchemaGenerator;

    @Test
    public void shouldSkipExecutionWhenSourceDirectoryDoesNotExist() throws Exception {
        final File nonExistentDir = new File(tempDir.toFile(), "does-not-exist");
        final File outputDir = tempDir.resolve("output").toFile();

        final EffectiveJsonSchemaMojo mojo = new EffectiveJsonSchemaMojo(
                objectFactory, project, nonExistentDir, outputDir);

        mojo.execute();

        verify(objectFactory, never()).catalogJsonSchemaLoader();
    }

    @Test
    public void shouldSkipExecutionWhenSourceDirectoryContainsNoJsonFiles() throws Exception {
        final Path sourceDir = tempDir.resolve("source");
        Files.createDirectories(sourceDir);
        Files.writeString(sourceDir.resolve("readme.txt"), "not a schema");

        when(project.getArtifacts()).thenReturn(Set.of());
        when(objectFactory.catalogJsonSchemaLoader()).thenReturn(catalogJsonSchemaLoader);
        when(catalogJsonSchemaLoader.loadAllJsonSchemas(anyList(), any())).thenReturn(Map.of());

        final EffectiveJsonSchemaMojo mojo = new EffectiveJsonSchemaMojo(
                objectFactory, project, sourceDir.toFile(), tempDir.resolve("output").toFile());

        mojo.execute();

        verify(objectFactory, never()).effectiveJsonSchemaGenerator();
    }

    @Test
    public void shouldCallGeneratorWithCollectedSchemasAndSchemaFiles() throws Exception {
        final Path sourceDir = tempDir.resolve("source");
        Files.createDirectories(sourceDir);
        Files.writeString(sourceDir.resolve("person.json"),
                "{ \"id\": \"http://example.com/person.json\" }");

        final Map<String, String> loadedSchemas = Map.of(
                "http://example.com/person.json", "{ \"id\": \"http://example.com/person.json\" }");

        when(project.getArtifacts()).thenReturn(Set.of());
        when(objectFactory.catalogJsonSchemaLoader()).thenReturn(catalogJsonSchemaLoader);
        when(catalogJsonSchemaLoader.loadAllJsonSchemas(anyList(), any())).thenReturn(loadedSchemas);
        when(objectFactory.effectiveJsonSchemaGenerator()).thenReturn(effectiveJsonSchemaGenerator);

        final File outputDir = tempDir.resolve("output").toFile();
        final EffectiveJsonSchemaMojo mojo = new EffectiveJsonSchemaMojo(
                objectFactory, project, sourceDir.toFile(), outputDir);

        mojo.execute();

        verify(effectiveJsonSchemaGenerator).generate(any(), any(), any(), any());
    }

    @Test
    public void shouldCollectJarArtifactsFromProjectAndPassToLoader() throws Exception {
        final Path sourceDir = tempDir.resolve("source");
        Files.createDirectories(sourceDir);
        Files.writeString(sourceDir.resolve("schema.json"), "{ \"id\": \"http://example.com/schema.json\" }");

        final File jarFile = tempDir.resolve("dep.jar").toFile();
        jarFile.createNewFile();

        final Artifact artifact = org.mockito.Mockito.mock(Artifact.class);
        when(artifact.getType()).thenReturn("jar");
        when(artifact.getFile()).thenReturn(jarFile);

        when(project.getArtifacts()).thenReturn(Set.of(artifact));
        when(objectFactory.catalogJsonSchemaLoader()).thenReturn(catalogJsonSchemaLoader);
        when(catalogJsonSchemaLoader.loadAllJsonSchemas(anyList(), any())).thenReturn(Map.of());
        when(objectFactory.effectiveJsonSchemaGenerator()).thenReturn(effectiveJsonSchemaGenerator);

        final EffectiveJsonSchemaMojo mojo = new EffectiveJsonSchemaMojo(
                objectFactory, project, sourceDir.toFile(), tempDir.resolve("output").toFile());

        mojo.execute();

        verify(catalogJsonSchemaLoader).loadAllJsonSchemas(
                org.mockito.ArgumentMatchers.argThat(list -> list.contains(jarFile)),
                any());
    }

    @Test
    public void shouldWrapEffectiveJsonSchemaGenerationExceptionInMojoExecutionException() throws Exception {
        final Path sourceDir = tempDir.resolve("source");
        Files.createDirectories(sourceDir);
        Files.writeString(sourceDir.resolve("schema.json"), "{ \"id\": \"http://example.com/schema.json\" }");

        when(project.getArtifacts()).thenReturn(Set.of());
        when(objectFactory.catalogJsonSchemaLoader()).thenReturn(catalogJsonSchemaLoader);
        when(catalogJsonSchemaLoader.loadAllJsonSchemas(anyList(), any())).thenReturn(Map.of());
        when(objectFactory.effectiveJsonSchemaGenerator()).thenReturn(effectiveJsonSchemaGenerator);
        org.mockito.Mockito.doThrow(new EffectiveJsonSchemaGenerationException("boom"))
                .when(effectiveJsonSchemaGenerator).generate(any(), any(), any(), any());

        final EffectiveJsonSchemaMojo mojo = new EffectiveJsonSchemaMojo(
                objectFactory, project, sourceDir.toFile(), tempDir.resolve("output").toFile());

        final MojoExecutionException exception = assertThrows(MojoExecutionException.class, mojo::execute);

        assertThat(exception.getMessage().contains("Effective JSON schema generation failed"), is(true));
    }
}

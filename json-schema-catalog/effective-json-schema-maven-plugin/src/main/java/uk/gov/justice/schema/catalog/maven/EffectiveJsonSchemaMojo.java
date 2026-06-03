package uk.gov.justice.schema.catalog.maven;

import static java.util.stream.Collectors.toList;
import static org.apache.maven.plugins.annotations.LifecyclePhase.PROCESS_SOURCES;
import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE;

import uk.gov.justice.schema.catalog.generation.effective.CatalogJsonSchemaLoader;
import uk.gov.justice.schema.catalog.generation.effective.EffectiveJsonSchemaGenerationException;
import uk.gov.justice.schema.catalog.generation.effective.EffectiveJsonSchemaGenerator;
import uk.gov.justice.schema.catalog.generation.effective.EffectiveJsonSchemaObjectFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Maven plugin goal that generates an <em>effective JSON schema</em> for each JSON schema file
 * in the configured source directory. The effective schema is a single self-contained document
 * with all external {@code $ref} references inlined, in the same spirit as Maven's
 * {@code help:effective-pom}.
 *
 * <p>Effective JSON schema files are written to {@code target/effective-json-schemas/} (or the
 * configured {@code outputDirectory}), preserving the relative directory structure of the
 * source directory.
 *
 * <p>This goal must run after the {@code generate-schema-catalog} goal so that the current
 * module's own schemas are available. It is therefore bound to the {@code process-sources} phase
 * by default.
 */
@Mojo(name = "generate-effective-json-schemas",
        requiresDependencyResolution = COMPILE,
        defaultPhase = PROCESS_SOURCES)
public class EffectiveJsonSchemaMojo extends AbstractMojo {

    @Parameter(required = true)
    private File sourceDirectory;

    @Parameter(defaultValue = "${project.build.directory}/effective-json-schemas")
    private File outputDirectory;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    private final EffectiveJsonSchemaObjectFactory objectFactory;

    public EffectiveJsonSchemaMojo() {
        this.objectFactory = new EffectiveJsonSchemaObjectFactory();
    }

    EffectiveJsonSchemaMojo(final EffectiveJsonSchemaObjectFactory objectFactory,
                             final MavenProject project,
                             final File sourceDirectory,
                             final File outputDirectory) {
        this.objectFactory = objectFactory;
        this.project = project;
        this.sourceDirectory = sourceDirectory;
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void execute() throws MojoExecutionException {
        if (!sourceDirectory.exists()) {
            getLog().warn("Source directory '" + sourceDirectory + "' does not exist — skipping effective JSON schema generation");
            return;
        }

        try {
            final List<File> dependencyJars = collectDependencyJars();
            final CatalogJsonSchemaLoader loader = objectFactory.catalogJsonSchemaLoader();
            final Map<String, String> allSchemasById = loader.loadAllJsonSchemas(
                    dependencyJars, sourceDirectory.toPath());

            final List<Path> schemaFiles = findJsonSchemaFiles(sourceDirectory.toPath());

            if (schemaFiles.isEmpty()) {
                getLog().info("No JSON schema files found in '" + sourceDirectory + "' — nothing to generate");
                return;
            }

            final EffectiveJsonSchemaGenerator generator = objectFactory.effectiveJsonSchemaGenerator();
            generator.generate(schemaFiles, sourceDirectory.toPath(), allSchemasById,
                    outputDirectory.toPath());

            getLog().info("Generated " + schemaFiles.size() + " effective JSON schema(s) in '"
                    + outputDirectory.getAbsolutePath() + "'");

        } catch (final EffectiveJsonSchemaGenerationException e) {
            throw new MojoExecutionException("Effective JSON schema generation failed", e);
        } catch (final IOException e) {
            throw new MojoExecutionException("Failed to scan source directory '" + sourceDirectory + "'", e);
        }
    }

    private List<File> collectDependencyJars() {
        return project.getArtifacts().stream()
                .filter(artifact -> "jar".equals(artifact.getType()))
                .map(Artifact::getFile)
                .filter(Objects::nonNull)
                .filter(File::exists)
                .filter(File::isFile)
                .collect(toList());
    }

    private List<Path> findJsonSchemaFiles(final Path sourceDir) throws IOException {
        return Files.walk(sourceDir)
                .filter(path -> path.toString().endsWith(".json"))
                .collect(toList());
    }
}

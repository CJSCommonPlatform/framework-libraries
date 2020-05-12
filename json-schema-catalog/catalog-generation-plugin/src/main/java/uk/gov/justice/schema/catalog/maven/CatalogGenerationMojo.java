package uk.gov.justice.schema.catalog.maven;

import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;
import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE_PLUS_RUNTIME;

import uk.gov.justice.maven.generator.io.files.parser.generator.GenerateMojo;

import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "generate-schema-catalog", requiresDependencyResolution = COMPILE_PLUS_RUNTIME, defaultPhase = GENERATE_SOURCES)
public class CatalogGenerationMojo extends GenerateMojo {
}

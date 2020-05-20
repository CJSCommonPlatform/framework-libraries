package uk.gov.justice.generation.pojo.maven;

import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;
import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE_PLUS_RUNTIME;

import uk.gov.justice.maven.generator.io.files.parser.generator.GenerateMojo;

import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "generate-pojos", requiresDependencyResolution = COMPILE_PLUS_RUNTIME, defaultPhase = GENERATE_SOURCES)
public class PojoGenerationMojo extends GenerateMojo {
}

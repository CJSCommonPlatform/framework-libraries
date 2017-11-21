package uk.gov.justice.schema.catalog.generation.maven;

import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.maven.plugins.annotations.Parameter;

public class CatalogGeneratorProperties implements GeneratorProperties {

    @Parameter
    private String catalogName;

    @Parameter(defaultValue = "json/schema/")
    private String jsonSchemaPath = "json/schema/";

    public String getCatalogName() {
        return catalogName;
    }

    public Path getJsonSchemaPath() {
        return Paths.get(jsonSchemaPath);
    }
}

package uk.gov.justice.schema.catalog.generation.maven;

import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * Object that gives the properties specified in the maven plugin xml
 */
public class CatalogGeneratorProperties implements GeneratorProperties {

    @Parameter
    private String catalogName;


    @Parameter(defaultValue = "json/schema/")
    private String jsonSchemaPath = "json/schema/";

    /**
     * @return The name of the catalog specified in the maven plugin xml
     */
    public String getCatalogName() {
        return catalogName;
    }

    /**
     * @return The path (on the classpath) to where the json schemas are located. Defaults to 'json/schema'
     */
    public Path getJsonSchemaPath() {
        return Paths.get(jsonSchemaPath);
    }
}

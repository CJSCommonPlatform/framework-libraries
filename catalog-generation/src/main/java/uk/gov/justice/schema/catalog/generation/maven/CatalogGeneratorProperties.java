package uk.gov.justice.schema.catalog.generation.maven;

import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorProperties;

import org.apache.maven.plugins.annotations.Parameter;

public class CatalogGeneratorProperties implements GeneratorProperties {

    @Parameter
    private String catalogName;

    public String getCatalogName() {
        return catalogName;
    }
}

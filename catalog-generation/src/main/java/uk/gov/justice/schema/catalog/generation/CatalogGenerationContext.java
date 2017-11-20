package uk.gov.justice.schema.catalog.generation;

public class CatalogGenerationContext {

    private static final String CATALOG_FILENAME = "schema_catalog.json";
    private static final String JSON_SCHEMA_PATH = "raml/json/schema/";
    public static final String AN_EMPTY_STRING = "";

    public String getCatalogFilename() {
        return CATALOG_FILENAME;
    }

    public String getJsonSchemaPath() {
        return JSON_SCHEMA_PATH;
    }
}

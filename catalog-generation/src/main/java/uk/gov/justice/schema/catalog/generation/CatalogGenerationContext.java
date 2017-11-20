package uk.gov.justice.schema.catalog.generation;

public class CatalogGenerationContext {

    private static final String CATALOG_GENERATION_ROOT = "target/generated-catalogs";
    private static final String CATALOG_PATH = "json/schema/catalog";
    private static final String CATALOG_FILENAME = "schema_catalog.json";
    private static final String JSON_SCHEMA_PATH = "raml/json/schema/";
    public static final String AN_EMPTY_STRING = "";

    public String getCatalogGenerationRoot() {
        return CATALOG_GENERATION_ROOT;
    }

    public String getCatalogPath() {
        return CATALOG_PATH;
    }

    public String getCatalogFilename() {
        return CATALOG_FILENAME;
    }

    public String getJsonSchemaPath() {
        return JSON_SCHEMA_PATH;
    }
}

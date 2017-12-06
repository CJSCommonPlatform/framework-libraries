package uk.gov.justice.schema.catalog.generation;

/**
 * Constants used in the Catalog generation
 */
public class CatalogGenerationContext {

    private static final String CATALOG_FILENAME = "schema_catalog.json";
    public static final String AN_EMPTY_STRING = "";

    /**
     * @return The file name of the json catalog.
     */
    public String getCatalogFilename() {
        return CATALOG_FILENAME;
    }

}

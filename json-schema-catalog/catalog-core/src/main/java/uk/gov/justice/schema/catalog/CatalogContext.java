package uk.gov.justice.schema.catalog;

/**
 * Constants used in the Catalog generation
 */
public class CatalogContext {

    private static final String CATALOG_LOCATION = "META-INF/";
    private static final String CATALOG_FILENAME = "schema_catalog.json";

    public static final String AN_EMPTY_STRING = "";

    /**
     * @return The file name of the json catalog.
     */
    public String getCatalogFilename() {
        return CATALOG_FILENAME;
    }

    /**
     * @return The location of the json catalog.
     */
    public String getCatalogLocation() {
        return CATALOG_LOCATION;
    }

    /**
     * @return The full path of the json catalog.
     */
    public String getCatalogFullPath() {
        return CATALOG_LOCATION + CATALOG_FILENAME;
    }
}

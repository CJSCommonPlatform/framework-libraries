package uk.gov.justice.generation.io.files.parser;

import org.everit.json.schema.Schema;

public class SchemaDefinition {

    private final String filename;
    private final Schema schema;

    public SchemaDefinition(final String filename, final Schema schema) {
        this.filename = filename;
        this.schema = schema;
    }

    public String getFilename() {
        return filename;
    }

    public Schema getSchema() {
        return schema;
    }
}

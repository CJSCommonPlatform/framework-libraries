package uk.gov.justice.generation.pojo.validation;

import org.everit.json.schema.Schema;

public class SchemaValidatorVisitableFactory {

    public SchemaValidatorVisitable create(final Schema schema) {
        return new SchemaValidatorVisitable(schema);
    }
}

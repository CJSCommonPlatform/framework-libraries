package uk.gov.justice.generation.pojo.core;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;

public class CombinedSchemaPropertyExtractor {

    public Map<String, Schema> getAllPropertiesFrom(final CombinedSchema combinedSchema) {

        final Map<String, Schema> propertySchemaMap = new HashMap<>();

        combinedSchema.getSubschemas().forEach(schema -> {
            if(schema instanceof ObjectSchema) {
                final ObjectSchema objectSchema = (ObjectSchema) schema;
                final Map<String, Schema> propertySchemas = objectSchema.getPropertySchemas();
                propertySchemaMap.putAll(propertySchemas);
            } else if (schema instanceof CombinedSchema) {
                final Map<String, Schema> propertySchemas = getAllPropertiesFrom((CombinedSchema) schema);
                propertySchemaMap.putAll(propertySchemas);
            } else {
                throw new UnsupportedOperationException(format("Schemas of type %s not supported as sub schemas of CombinedSchema", schema.getClass().getSimpleName()));
            }
        });

        return propertySchemaMap;
    }
}

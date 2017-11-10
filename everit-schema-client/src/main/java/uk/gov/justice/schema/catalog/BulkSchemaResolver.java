package uk.gov.justice.schema.catalog;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BulkSchemaResolver extends DictionarySchemaResolver {

    //private static SchemaDictionary schemaDictionary = new SchemaDictionary();

    private static final Logger LOG = LoggerFactory.getLogger(BulkSchemaResolver.class);
    private final SchemaDefinitionCache schemaDefinitionCache;

    public BulkSchemaResolver(SchemaDefinitionCache cache) {
        this.schemaDefinitionCache = cache;
    }

    @Override
    public InputStream get(String url) {

        LOG.info("Resolving schema: {}", url);

        //Resolve for other IDs and translations
        //TODO
        String id = url;

        if (schemaDefinitionCache.contains(id)) {
            //being defined or re-defined

            //test for schema being re-defined
            //if (schemaDictionary.contains(id))

            LOG.info("Schema dependency found in the bulk definition cache", url);

            String jsonSchemaDefinition = schemaDefinitionCache.getSchema(id);
            return convertStringToInputStream(jsonSchemaDefinition);


        } else {
            return super.get(url);
        }

    }

}

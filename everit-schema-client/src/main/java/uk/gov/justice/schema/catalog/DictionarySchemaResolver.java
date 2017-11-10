package uk.gov.justice.schema.catalog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DictionarySchemaResolver implements SchemaClient {

    protected static final SchemaDictionary schemaDictionary = new SchemaDictionary();

    private static final Logger LOG = LoggerFactory.getLogger(DictionarySchemaResolver.class);

    @Override
    public InputStream get(String url) {

        LOG.info("Resolving schema: %s", url);

        //Resolve for other IDs and translations
        //TODO
        String id = url;

        //simple resolve
        Schema schema = schemaDictionary.getSchema(id);

        if (schema == null) {
            LOG.info("Schema: {} not found in the dictionary", url);
            return null;
        } else {
            LOG.info("Dictionary hit for schema: {}", url);
            return convertStringToInputStream(schema.toString());
        }


    }

    public static InputStream convertStringToInputStream(String schemaString) {
        if (schemaString == null) {
            return null;
        } else {
            //handle UTF and charsets
            return new ByteArrayInputStream(schemaString.getBytes());
        }
    }


}

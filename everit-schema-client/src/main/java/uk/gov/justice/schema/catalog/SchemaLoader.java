package uk.gov.justice.schema.catalog;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SchemaLoader {

    @Inject
    SchemaDictionary schemaDictionary;

    private static final Logger LOG = LoggerFactory.getLogger(SchemaLoader.class);

    private static final String UTF_8 = "UTF-8";

    private static final Charset CHARSET_UTF8 = Charset.forName(UTF_8);

    private static final String PROTOCOL_FILE = "file";
    private static final String PROTOCOL_JAR = "jar";
    private static final String PROTOCOL_VFS = "vfs";



    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
    }

    @PostConstruct
    private void startup() {
        final String message = "Application has started with resources %s";
        LOG.info(format(message, schemaDictionary));
    }

    public String readSchema(URL url) throws IOException {

        InputStream in = null;
        String schemaString = null;
        try {
            if (url.getProtocol().toLowerCase().equals(PROTOCOL_JAR) || url.getProtocol().toLowerCase().equals(PROTOCOL_VFS)) {
                in = url.openStream();
            } else if (url.getProtocol().toLowerCase().equals(PROTOCOL_FILE)) {
                in = SchemaLoader.class.getResourceAsStream(url.getFile());
            }


            schemaString = IOUtils.toString(in, CHARSET_UTF8);

        } catch (IOException e) {
            LOG.error(format("Failed to read the schema url: %s", url.toString()), e);
            throw e;
        } finally {
            IOUtils.closeQuietly(in);
        }
        return schemaString;
    }

    public void loadSchema(URL url) throws IOException {
        String jsonSchema = readSchema(url);
        SchemaClient resolver = new DictionarySchemaResolver();


        Schema schema = org.everit.json.schema.loader.SchemaLoader.builder()

                .schemaJson(
                        new JSONObject(new JSONTokener(jsonSchema))).httpClient(resolver)
                .build().load().build();
        String id = schema.getId();
        schemaDictionary.putSchema(id, schema);

    }


    public void bulkLoadSchemas(Map<String, URL> schemaLocationMap) {
        if (schemaLocationMap == null || schemaLocationMap.isEmpty()) {
            LOG.info("Bulk load didn't happen since the schema map given is empty");
            return;
        }
        SchemaDefinitionCache cache = new SchemaDefinitionCache();
        for (String id : schemaLocationMap.keySet()
                ) {
            try {
                String jsonDefinition = readSchema(schemaLocationMap.get(id));
                cache.putSchema(id, jsonDefinition);
            } catch (IOException e) {
                cache.clear();
                return;
            }
        }

        processBulkLoadCache(cache);

    }

    private void processBulkLoadCache(SchemaDefinitionCache cache) {
        LOG.info("Reference for schemaDictionary is: {}", schemaDictionary);
        SchemaClient resolver = new BulkSchemaResolver(cache);
        int count = 0;
        for (String id : cache.getIDSet()
                ) {
            Schema schema = org.everit.json.schema.loader.SchemaLoader.builder()

                    .schemaJson(
                            new JSONObject(new JSONTokener(cache.getSchema(id)))).httpClient(resolver)
                    .build().load().build();
            schemaDictionary.putSchema(id, schema);
            count++;
        }
        LOG.info("Loaded {} schema(s) to the schema dictionary. Now contains {} schema(s)", count, schemaDictionary.size());
        cache.clear();
    }

}

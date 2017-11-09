package uk.gov.justice;

import static java.lang.String.format;
import static java.nio.file.Paths.get;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.gov.justice.services.messaging.JsonEnvelope.METADATA;

import uk.gov.justice.services.core.json.DefaultJsonSchemaValidator;
import uk.gov.justice.services.core.json.JsonSchemaValidator;
import uk.gov.justice.services.core.json.SchemaLoadingException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;

public class ValidateService implements JsonSchemaValidator {

    private final Map<String, Schema> schemas = new ConcurrentHashMap<>();


    private Logger logger = getLogger(ValidateService.class);

    DefaultJsonSchemaValidator validator = new DefaultJsonSchemaValidator();

    public Schema load(final String schemaFile) {
        logger.trace("Loading schema {}", schemaFile);
        try (final InputStream schemaFileStream = this.getClass().getResourceAsStream(schemaFile)){
            final URL schemaUrl = getClass().getResource(schemaFile);

            Schema schema = SchemaLoader.builder()
                    .resolutionScope(resolveUrl(schemaUrl))
                    .schemaJson(
                            new JSONObject(new JSONTokener(schemaFileStream))).httpClient(new CPPSchemaClient())
                    .build().load().build();
            System.out.println("*********************************");
            System.out.println(schema.toString());
            System.out.println("*********************************");
            return schema;
        } catch (final Exception ex) {
            throw new SchemaLoadingException(format("Unable to load JSON schema %s from classpath", schemaFile), ex);
        }
    }

    private String resolveUrl(final URL schemaUrl) throws URISyntaxException, IOException {
        return getPhysicalFrom(schemaUrl).toString();
    }

    public URL getPhysicalFrom(final URL url) throws URISyntaxException, IOException {
            return new URL(format("file:%s/", get(url.toURI()).getParent()));
    }

    @Override
    public void validate(final String payload, final String name) {
        logger.trace("Performing schema validation for: {}", name);
        final JSONObject jsonObject = new JSONObject(payload);
        jsonObject.remove(METADATA);
        schemaOf(name).validate(jsonObject);
    }

    private Schema schemaOf(final String name) {
        Schema s = schemas.get(name);
        if (s==null) {
            s = loadSchema(name);
            schemas.put(name, s);
        }
        return s;
    }

    private final static String SCHEMA_LOCATION_PATTERN = "/json/schema/%s.json";


    public Schema loadSchema(final String name) {
        final String schemaFile = format(SCHEMA_LOCATION_PATTERN, name);
        return load(schemaFile);
    }
}

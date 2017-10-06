package uk.gov.justice;

import static java.lang.String.format;

import uk.gov.justice.services.core.json.SchemaLoadingException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SchemaDictionary {


    public Map<String, String> schemas = new ConcurrentHashMap<String, String>();

    public SchemaDictionary() {

    }


    public void loadSchema(final String schemaFile) throws Exception{
        Schema schema = null;
        try (final InputStream schemaFileStream = this.getClass().getResourceAsStream(schemaFile)) {
            final URL schemaUrl = getClass().getResource(schemaFile);

            schema = SchemaLoader.builder()
                    .resolutionScope(resolveUrl(schemaUrl))
                    .schemaJson(
                            new JSONObject(new JSONTokener(schemaFileStream))).httpClient(new CPPSchemaClient())
                    .build().load().build();
            System.out.println("schema defines: " + schema.getId());
            final InputStream inputStream = this.getClass().getResourceAsStream(schemaFile);
            schemas.put(schema.getId(), new String(convertToBytes(inputStream)));

        } catch (final Exception ex) {
            throw new SchemaLoadingException(format("Unable to load JSON schema %s from classpath", schemaFile), ex);
        }

    }

    public byte[] convertToBytes(InputStream is) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bucket = new byte[1024];
        int i = 0;
        while ((i = is.read(bucket)) >= 0) {
            baos.write(bucket, 0, i);
        }
        return baos.toByteArray();

    }

    private String resolveUrl(final URL schemaUrl) throws URISyntaxException, IOException {
        return getPhysicalFrom(schemaUrl).toString();
    }

    public URL getPhysicalFrom(final URL url) throws URISyntaxException, IOException {
        return new URL(format("file:%s/", Paths.get(url.toURI()).getParent()));
    }


    public InputStream get(String url) {
        String schemaString = schemas.get(url);
        if (schemaString==null)
        {
            return null;
        }
        else
        {
            //handle UTF and charsets
            return new ByteArrayInputStream(schemaString.getBytes());
        }

    }
}

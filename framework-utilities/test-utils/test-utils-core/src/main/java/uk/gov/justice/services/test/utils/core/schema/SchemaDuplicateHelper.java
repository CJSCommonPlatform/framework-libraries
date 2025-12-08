package uk.gov.justice.services.test.utils.core.schema;

import static java.lang.String.format;
import static uk.gov.justice.services.test.utils.core.messaging.JsonObjects.jsonReaderFactory;

import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.commons.codec.digest.DigestUtils;

public class SchemaDuplicateHelper {

    public static final String WINDOWS_NEWLINE = "\r\n";
    public static final String UNIX_NEWLINE = "\n";

    public static final String JSON_SCHEMA_FILE_EXTENSION = ".json";
    public static final String JSON_SCHEMA_BASE_PATH = "json/schema/";
    public static final String FILE_PROTOCOL = "file";
    public static final String JAR_PROTOCOL = "jar";

    public static final String JAR_FILE_EXTENSION = ".jar";

    private SchemaDuplicateHelper() {
    }

    /**
     * Extracts the filesystem jar path from a Jar URL
     * @param url
     * @return
     */
    public static String extractJarPath(URL url) {
        if (!url.getProtocol().equalsIgnoreCase(JAR_PROTOCOL)) {
            throw new SchemaCheckerException(format("Unable to extract the JAR path from a url which does not use a jar protocol: %s", url.toString()));
        }
        final String FILE_PROTOCOL_START = FILE_PROTOCOL + ":";
        String path = url.getPath();
        int indexStartPos = path.indexOf(FILE_PROTOCOL_START);
        int indexEndPos = path.indexOf(JAR_FILE_EXTENSION + "!/" + JSON_SCHEMA_BASE_PATH);
        if (indexEndPos < 0 || indexStartPos < 0) {
            throw new SchemaCheckerException(format("Unable to extract the JAR path from: %s", url.toString()));
        }
        return path.substring(indexStartPos + FILE_PROTOCOL_START.length(), indexEndPos + JAR_FILE_EXTENSION.length());
    }

    /**
     * Tests if a file is a jar (based on the extension)
     * @param f
     * @return
     */
    public static boolean isJAR(File f) {
        return f.isFile() && f.getName().toLowerCase().endsWith(JAR_FILE_EXTENSION);
    }

    /**
     * Strips the whitespace from a Json string
     * @param jsonString
     * @return
     */
    public static String stripWhitespace(String jsonString) {
        try (final JsonReader jsonReader = jsonReaderFactory.createReader(new StringReader(jsonString))) {
            JsonObject jsonObject = jsonReader.readObject();
            return jsonObject.toString();
        }
    }

    /**
     * Computes the Sha 256 hash from a string
     * @param string
     * @return
     */
    public static String computeSha256Checksum(String string) {
        return DigestUtils.sha256Hex(string);
    }

    /**
     * Splits the file contents into multiple lines (supports Unix and Windows)
     * @param fileContents
     * @return
     */
    public static List<String> convertFileContentToLines(String fileContents) {
        String newlineDelimiter = UNIX_NEWLINE;
        if (fileContents.indexOf(WINDOWS_NEWLINE) > -1) {
            //windows delimited file
            newlineDelimiter = WINDOWS_NEWLINE;
        }
        return Arrays.asList(fileContents.split(newlineDelimiter));
    }
}

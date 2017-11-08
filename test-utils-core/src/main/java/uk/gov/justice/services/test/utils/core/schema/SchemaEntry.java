package uk.gov.justice.services.test.utils.core.schema;

import java.net.URL;

public class SchemaEntry {

    private URL url = null;

    private String filename = null;

    private String checksum = null;

    public SchemaEntry(URL url, String filename) {
        this.url = url;
        this.filename = filename;
    }

    public URL getUrl() {
        return url;
    }

    public String getFilename() {
        return filename;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}

package uk.gov.justice.services.test.utils.core.schema;

import java.net.URL;

import org.apache.commons.codec.digest.DigestUtils;

public class SchemaEntryTestVO extends  SchemaEntry {

    private String fileContents = null;

    private String fileContentTestCalculatedHash = null;

    public SchemaEntryTestVO(URL url, String filename, String fileContents) {
        super(url, filename);
        this.fileContents = fileContents;
        this.fileContentTestCalculatedHash = DigestUtils.sha256Hex(fileContents);
    }

    public String getFileContents() {
        return fileContents;
    }

    public String getFileContentTestCalculatedHash() {
        return fileContentTestCalculatedHash;
    }
}

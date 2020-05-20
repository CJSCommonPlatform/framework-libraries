package uk.gov.justice.services.test.utils.core.schema;

import static uk.gov.justice.services.test.utils.core.schema.SchemaDuplicateHelper.computeSha256Checksum;
import static uk.gov.justice.services.test.utils.core.schema.SchemaDuplicateHelper.isJAR;
import static uk.gov.justice.services.test.utils.core.schema.SchemaDuplicateHelper.stripWhitespace;

import java.io.File;

public class SchemaFile {

    private File containerFile = null;

    private File file = null;

    private String originalFileContents = null;

    private String originalFileChecksum = null;

    private String prettyPrintChecksum = null;

    public SchemaFile(File containerFile, File file) {
        this.containerFile = containerFile;
        this.file = file;
    }

    public SchemaFile(File containerFile, File file, String originalFileContents) {
        this(containerFile, file);
        setOriginalFileContents(originalFileContents);
    }

    public void setOriginalFileContents(String originalFileContents) {
        this.originalFileContents = originalFileContents;
        this.originalFileChecksum = computeSha256Checksum(originalFileContents);
        this.prettyPrintChecksum = computeSha256Checksum(stripSchemaWhiteSpace());
    }

    public String stripSchemaWhiteSpace() {
        return stripWhitespace(this.originalFileContents);
    }

    public boolean isInsideJar() {
        return isJAR(this.containerFile);
    }

    public File getContainerFile() {
        return containerFile;
    }

    public File getFile() {
        return file;
    }

    public String getSchemaFileName() {
        return file.getName();
    }

    public String getOriginalFileContents() {
        return originalFileContents;
    }

    public String getOriginalFileChecksum() {
        return originalFileChecksum;
    }

    public String getPrettyPrintChecksum() {
        return prettyPrintChecksum;
    }
}

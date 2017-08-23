package uk.gov.justice.generation.pojo.validation;

import java.io.File;

public class FileNameValidator {

    public boolean isEventSchema(final File jsonSchemaFile) {
        final String filename = jsonSchemaFile.getName();

        return filename.matches("(.+)\\.events\\.(.+)");
    }
}

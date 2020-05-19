package uk.gov.justice.generation.pojo.validation;

import java.io.File;

/**
 * Validates that the name of the json schema document is correct
 */
public class FileNameValidator {

    /**
     * Will return true if the name of the json schema shows that the
     * event annotation should be applied to the generated root class
     *
     * @param jsonSchemaFile The name of the json schema file
     * @return true if the Event annotation should be added to the class
     */
    public boolean isEventSchema(final File jsonSchemaFile) {
        final String filename = jsonSchemaFile.getName();

        return filename.matches("(.+)\\.events\\.(.+)");
    }
}

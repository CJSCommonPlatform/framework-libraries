package uk.gov.justice.generation.pojo.plugin.namegeneratable;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.everit.json.schema.Schema;

/**
 * Generate the root class name from a schema filename.
 *
 * The filename extension is removed, any prepending '.' are removed from the filename and then all
 * '-' are removed and the first letter after each '-' is capitalized.
 *
 * For example:
 *
 * context.something-has-happened.json
 *
 * becomes:
 *
 * somethingHasHappened
 */
public class RootNameGeneratorPlugin implements NameGeneratablePlugin {

    @Override
    public String rootFieldNameFrom(final Schema schema, final String schemaFilename) {

        final String fileName = getValidFileNameWithNoExtension(schemaFilename);

        final String name = getNameFrom(fileName);

        if (name.isEmpty()) {
            throw new NameGenerationException(format("Failed to load json schema file '%s'. File name is invalid", schemaFilename));
        }

        final String className = Stream.of(name.split("-")).map(StringUtils::capitalize).collect(joining());

        return uncapitalize(className);
    }

    private String getValidFileNameWithNoExtension(final String schemaFilename) {

        if (!schemaFilename.endsWith(".json")) {
            throw new NameGenerationException(format("Failed to load json schema file '%s'. File does not have a '.json' extension", schemaFilename));
        }

        return removeFileExtensionFrom(schemaFilename);
    }

    private String removeFileExtensionFrom(final String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    private String getNameFrom(final String fileName) {
        final int index = fileName.lastIndexOf('.');
        if (index > -1) {
            return fileName.substring(index + 1);
        }

        return fileName;
    }
}

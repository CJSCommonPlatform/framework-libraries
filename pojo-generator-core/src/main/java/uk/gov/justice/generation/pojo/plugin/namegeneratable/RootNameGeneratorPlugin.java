package uk.gov.justice.generation.pojo.plugin.namegeneratable;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

import uk.gov.justice.generation.pojo.plugin.classmodifying.PluginContext;

import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.everit.json.schema.Schema;

/**
 * Generate the root class name from a schema filename or from a rootClassName set in
 * generatorProperties.
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
 *
 * The root class name can also be set by adding the rootClassName tag to the generatorProperties
 * tag in the Maven Plugin configuration.
 *
 * <pre>
 * {@code
 *
 *   <configuration>
 *     ...
 *     <generatorProperties>
 *        <rootClassName>ClassNameValue</rootClassName>
 *     </generatorProperties>
 *     ...
 *   </configuration>
 *
 * }
 * </pre>
 */
public class RootNameGeneratorPlugin implements NameGeneratablePlugin {

    private static final String ROOT_CLASS_NAME_PROPERTY = "rootClassName";

    @Override
    public String rootFieldNameFrom(final Schema schema, final String schemaFilename, final PluginContext pluginContext) {
        final Optional<String> rootClassName = pluginContext.generatorPropertyValueOf(ROOT_CLASS_NAME_PROPERTY);

        return uncapitalize(rootClassName.orElseGet(() -> parseClassNameFromFilename(schemaFilename)));
    }

    private String parseClassNameFromFilename(final String schemaFilename) {
        final String fileName = getValidFileNameWithNoExtension(schemaFilename);

        final String name = getNameFrom(fileName);

        if (name.isEmpty()) {
            throw new NameGenerationException(format("Failed to load json schema file '%s'. File name is invalid", schemaFilename));
        }

        return Stream.of(name.split("-")).map(StringUtils::capitalize).collect(joining());
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

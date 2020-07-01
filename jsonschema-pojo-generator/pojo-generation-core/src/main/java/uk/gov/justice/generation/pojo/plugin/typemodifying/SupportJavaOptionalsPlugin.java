package uk.gov.justice.generation.pojo.plugin.typemodifying;

import static com.squareup.javapoet.ClassName.get;
import static java.lang.String.format;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ARRAY;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.plugin.IncompatiblePluginException;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.MakeClassSerializablePlugin;

import java.util.List;
import java.util.Optional;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

/**
 * A type modifying plugin for setting all properties which are not marked as <i>required</i> in the
 * json schema document to be wrapped in a java {@link Optional}.
 *
 * For example:
 *
 * <pre>
 *     {@code
 *
 *      public class MyClass {
 *
 *          private final String myProperty;
 *
 *          public MyClass(final String myProperty) {
 *              this.myProperty = myProperty;
 *          }
 *
 *          public Optional<String> getMyProperty() {
 *              return Optional.ofNullable(myProperty);
 *          }
 *      }
 *
 * }
 * </pre>
 */
public class SupportJavaOptionalsPlugin implements TypeModifyingPlugin {

    @Override
    public TypeName modifyTypeName(
            final TypeName originalTypeName,
            final Definition definition,
            final PluginContext pluginContext) {

        if (shouldAddOptional(definition)) {
            return ParameterizedTypeName.get(get(Optional.class), originalTypeName);
        }

        return originalTypeName;
    }

    @Override
    public void checkCompatibilityWith(final List<String> pluginNames) {

        final String incompatiblePluginClassName = MakeClassSerializablePlugin.class.getName();

        if (pluginNames.contains(incompatiblePluginClassName)) {
            throw new IncompatiblePluginException(format(
                    "Plugin '%s' is incompatible with plugin '%s' and should not be run together",
                    getClass().getName(),
                    incompatiblePluginClassName));
        }
    }

    private boolean shouldAddOptional(final Definition definition) {
        return definition.type() != ARRAY && !definition.isRequired();
    }
}

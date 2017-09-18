package uk.gov.justice.generation.pojo.plugin.typemodifying;

import static com.squareup.javapoet.ClassName.get;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ARRAY;

import uk.gov.justice.generation.pojo.dom.Definition;

import java.util.Optional;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

/**
 * A type modifying plugin for setting all properties which are not marked as <i>required</i>
 * in the json schema document to be wrapped in a java {@link Optional}.
 *
 * For example:
 *
 * <pre>
 *     {@code public class MyClass {
 *
 *          private final Optional<String> myProperty;
 *
 *          public MyClass(final Optional<String> myProperty) {
 *              this.myProperty = myProperty;
 *          }
 *
 *          public Optional<String> getMyProperty() {
 *              return myProperty;
 *          }
 *      }
 *
 * }</pre>
 */
public class SupportJavaOptionalsPlugin implements TypeModifyingPlugin {

    @Override
    public TypeName modifyTypeName(final TypeName originalTypeName, final Definition definition) {

        if (shouldAddOptional(definition)) {
            return ParameterizedTypeName.get(get(Optional.class), originalTypeName);
        }

        return originalTypeName;
    }

    private boolean shouldAddOptional(final Definition definition) {
        return definition.type() != ARRAY && !definition.isRequired();
    }
}

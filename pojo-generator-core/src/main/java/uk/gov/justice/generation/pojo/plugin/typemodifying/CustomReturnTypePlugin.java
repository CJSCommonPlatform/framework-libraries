package uk.gov.justice.generation.pojo.plugin.typemodifying;

import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;
import uk.gov.justice.generation.pojo.plugin.FactoryMethod;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.typemodifying.custom.CustomReturnTypeMapper;
import uk.gov.justice.generation.pojo.plugin.typemodifying.custom.FullyQualifiedNameToClassNameConverter;
import uk.gov.justice.generation.pojo.visitor.ReferenceValue;

import java.util.Optional;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

/**
 * Adds support for using custom java types as return types and constructor parameters in the
 * generated class.
 *
 * To Use: Set the typeMappings tag in the generatorProperties tag.  The name of the type mapping
 * nested tag corresponds to the name of the definition being referenced in the schema and the value
 * of the tag is the Java Class type to be used when generating the Java POJO.  For example:
 *
 * <pre>
 *      {@code
 *
 *              <typeMappings>
 *                  <date>java.time.ZonedDateTime</date>
 *              </typeMappings>
 *      }
 * </pre>
 *
 * "date" will be the definition name and is referenced in your json schema file:
 *
 * <pre>
 *      {@code
 *
 *              "myProperty": {
 *                  "$ref": "#/definitions/date"
 *              },
 *              "definitions": {
 *                  "date": {
 *                      "type": "string",
 *                      "pattern": "^[1|2][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]$"
 *                  }
 *              }
 *     }
 * </pre>
 *
 * <p>This will generate the following code:</p>
 *
 * <pre>
 *          {@code
 *
 *              public class MyClass {
 *
 *                  private final ZonedDateTime myProperty;
 *
 *                  public MyClass(final ZonedDateTime myProperty) {
 *                      this.myProperty = myProperty;
 *                  }
 *
 *                  public ZonedDateTime getMyProperty() {
 *                      return myProperty;
 *                  }
 *              }
 *          }
 * </pre>
 */
public class CustomReturnTypePlugin implements TypeModifyingPlugin {

    private final CustomReturnTypeMapper customReturnTypeMapper;

    public CustomReturnTypePlugin(final CustomReturnTypeMapper customReturnTypeMapper) {
        this.customReturnTypeMapper = customReturnTypeMapper;
    }

    @FactoryMethod
    public static CustomReturnTypePlugin create() {

        final FullyQualifiedNameToClassNameConverter fullyQualifiedNameToClassNameConverter =
                new FullyQualifiedNameToClassNameConverter();
        final CustomReturnTypeMapper customReturnTypeMapper =
                new CustomReturnTypeMapper(fullyQualifiedNameToClassNameConverter);

        return new CustomReturnTypePlugin(customReturnTypeMapper);
    }

    /**
     * Modifies the class name to that specified by the schema reference value
     *
     * @param typeName      The type name to be modified
     * @param definition    The FieldDefinition of the type to be modified
     * @param pluginContext The {@link PluginContext}
     * @return The type name as taken from the schema reference value
     */
    @Override
    public TypeName modifyTypeName(
            final TypeName typeName,
            final Definition definition,
            final PluginContext pluginContext) {

        if (REFERENCE.equals(definition.type())) {
            final ReferenceDefinition referenceDefinition = (ReferenceDefinition) definition;
            final ReferenceValue referenceValue = referenceDefinition.getReferenceValue();
            final Optional<ClassName> className = customReturnTypeMapper.customType(referenceValue, pluginContext);

            if (className.isPresent()) {
                return className.get();
            }
        }

        return typeName;
    }
}

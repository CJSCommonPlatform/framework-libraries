package uk.gov.justice.generation.pojo.plugin.typemodifying;

import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;
import uk.gov.justice.generation.pojo.plugin.FactoryMethod;
import uk.gov.justice.generation.pojo.plugin.classmodifying.PluginContext;

import java.time.ZonedDateTime;

import com.squareup.javapoet.TypeName;

/**
 * Adds support for using custom java types as return types and constructor parameters in the generated
 * class.
 *
 * <p>To Use:</p>
 *
 * <p>The custom type should be specified as a reference in your json schema file:</p>
 *
 *      {@code
 *              "myProperty": {
 *                  "$ref": "#/definitions/java.time.ZonedDateTime"
 *              },
 *              "definitions": {
 *                  "java.time.ZonedDateTime": {
 *                      "type": "string",
 *                      "pattern": "^[1|2][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]$"
 *                  }
 *              }
 *     }
 *
 * The name {@code ZonedDateTime} in the definition is important as this is how we specify that
 * the types should be of type {@link ZonedDateTime}.
 *
 * NB: It is important to use the fully qualified class name as the package name is required to
 * create the {@link TypeName}
 *
 * <p>This will generate the following code:</p>
 *
 *          {@code public class MyClass {
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
 * }
 */
public class CustomReturnTypePlugin implements TypeModifyingPlugin {

    private final ReferenceToClassNameConverter referenceToClassNameConverter;

    public CustomReturnTypePlugin(final ReferenceToClassNameConverter referenceToClassNameConverter) {
        this.referenceToClassNameConverter = referenceToClassNameConverter;
    }

    @FactoryMethod
    public static CustomReturnTypePlugin create() {
        return new CustomReturnTypePlugin(new ReferenceToClassNameConverter());
    }

    /**
     * Modifies the class name to that specified by the schema reference value
     *
     * @param typeName The type name to be modified
     * @param definition The FieldDefinition of the type to be modified
     * @param pluginContext The {@link PluginContext}
     *
     * @return The type name as taken from the schema reference value
     */
    @Override
    public TypeName modifyTypeName(
            final TypeName typeName,
            final Definition definition,
            final PluginContext pluginContext) {

        if(REFERENCE.equals(definition.type())) {
            final ReferenceDefinition referenceDefinition = (ReferenceDefinition) definition;
            final String referenceValue = referenceDefinition.getReferenceValue();
            return referenceToClassNameConverter.get(referenceValue);
        }

        return typeName;
    }
}

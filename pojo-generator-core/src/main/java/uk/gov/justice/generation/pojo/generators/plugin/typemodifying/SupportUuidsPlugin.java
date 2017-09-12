package uk.gov.justice.generation.pojo.generators.plugin.typemodifying;

import static com.squareup.javapoet.ClassName.get;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;

import java.util.UUID;

import com.squareup.javapoet.TypeName;

/**
 * Adds support for using {@see UUID} as return types and constructor parameters in the generated
 * class.
 *
 * To Use:
 * <p>
 *     The uuid should be specified as a reference in your json schema file:
 *     <pre>
 *         {@code "myProperty": {
 *                  "$ref": "#/definitions/UUID"
 *              },
 *              "definitions": {
 *                  "UUID": {
 *                      "type": "string",
 *                      "pattern": "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
 *                  }
 *              }
 *
 *     }</pre>
 * </p>
 *
 * The name {@code UUID} in the definition is important as this is how we specify that
 * the types should be of type {@link UUID}.
 *
 * <p>
 *      This will generate the following code:
 *      <pre>
 *          {@code
 *  public class MyClass {
 *
 *                  private final UUID myProperty;
 *
 *                  public MyClass(final UUID myProperty) {
 *                      this.myProperty = myProperty;
 *                  }
 *
 *                  public UUID getMyProperty() {
 *                      return myProperty;
 *                  }
 *              }
 * }</pre>
 * </p>
 *
 */
public class SupportUuidsPlugin implements TypeModifyingPlugin {

    @Override
    public TypeName modifyTypeName(final TypeName typeName, final Definition definition) {
        if (shouldBeUuidTypeName(definition)) {
            return get(UUID.class);
        }

        return typeName;
    }

    private boolean shouldBeUuidTypeName(final Definition definition) {
        return REFERENCE.equals(definition.type()) &&
                ((ReferenceDefinition) definition).getReferenceValue().endsWith(UUID.class.getSimpleName());
    }
}

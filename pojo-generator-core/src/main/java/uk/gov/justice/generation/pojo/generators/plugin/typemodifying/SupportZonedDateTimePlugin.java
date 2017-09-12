package uk.gov.justice.generation.pojo.generators.plugin.typemodifying;

import static com.squareup.javapoet.TypeName.get;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;

import java.time.ZonedDateTime;

import com.squareup.javapoet.TypeName;

/**
 * Adds support for using {@see ZonedDateTime} as return types and constructor parameters in the generated
 * class.
 *
 * To Use:
 * <p>
 *     The ZonedDateTime should be specified as a reference in your json schema file:
 *     <pre>
 *         {@code "myProperty": {
 *                  "$ref": "#/definitions/ZonedDateTime"
 *              },
 *              "definitions": {
 *                  "ZonedDateTime": {
 *                      "type": "string",
 *                      "pattern": "^[1|2][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]$"
 *                  }
 *              }
 *
 *     }</pre>
 * </p>
 *
 * The name {@code ZonedDateTime} in the definition is important as this is how we specify that
 * the types should be of type {@link ZonedDateTime}.
 *
 * <p>
 *      This will generate the following code:
 *      <pre>
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
 * }</pre>
 * </p>
 *
 */

public class SupportZonedDateTimePlugin implements TypeModifyingPlugin {

    @Override
    public TypeName modifyTypeName(final TypeName typeName, final Definition definition) {
        if (shouldBeZonedDateTimeTypeName(definition)) {
            return get(ZonedDateTime.class);
        }

        return typeName;
    }

    private boolean shouldBeZonedDateTimeTypeName(final Definition definition) {
        return REFERENCE.equals(definition.type()) &&
                ((ReferenceDefinition) definition).getReferenceValue().endsWith(ZonedDateTime.class.getSimpleName());
    }
}

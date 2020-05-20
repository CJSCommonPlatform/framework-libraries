package uk.gov.justice.generation.pojo.dom;

import static uk.gov.justice.generation.pojo.dom.DefinitionType.ENUM;

import java.util.List;

/**
 * Defines an enum as specified in the json schema.
 *
 * For example the following definition in the schema
 * <pre>
 * {@code
 *
 *  {
 *      "enum": ["Red", "Blue", "Green"]
 *  }
 * }
 * </pre>
 *
 * would be rendered as
 *
 * <pre>
 * {@code
 *
 *  public enum FavouriteColour {
 *
 *      RED("Red"),
 *      BLUE("Blue"),
 *      GREEN("Green");
 *
 *      private final String value;
 *
 *      FavouriteColour(String value) {
 *          this.value = value;
 *      }
 *  }
 * }
 * </pre>
 *
 * Please note: an enum in json can support divers data types (mixing numbers with strings or
 * booleans for example) whereas a java enum the members must be of the same type. For this
 * reason only enums of {@link String}s are supported by this application
 */
public class EnumDefinition extends ClassDefinition {

    private final List<Object> enumValues;

    public EnumDefinition(final String fieldName, final List<Object> enumValues, final String id) {
        super(ENUM, fieldName, id);
        this.enumValues = enumValues;
    }

    /**
     * Gets the list of enumerated values
     * 
     * @return The list of enumerated values
     */
    public List<Object> getEnumValues() {
        return enumValues;
    }
}

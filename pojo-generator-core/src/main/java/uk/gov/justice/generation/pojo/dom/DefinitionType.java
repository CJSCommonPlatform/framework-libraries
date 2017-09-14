package uk.gov.justice.generation.pojo.dom;

/**
 * Defines the type of the property specified in the JSON schema file.
 */
public enum DefinitionType {

    /**
     * This property was defined as an array in the json schema:
     *
     * {@code { "type": "array" } }
     *
     * The resulting field in the generated POJO will be generated as a {@link java.util.List}
     */
    ARRAY,

    /**
     * This property was defined as an boolean in the json schema:
     *
     * {@code { "type": "boolean" } }
     *
     * The resulting field in the generated POJO will be generated as a {@link Boolean}
     */
    BOOLEAN,

    /**
     * This property was defined as an object in the json schema:
     *
     * {@code { "type": "object" } }
     *
     * The properties of this object will be generated as their own java POJO class
     */
    CLASS,

    /**
     * This property was defined as either allOf, oneOf or oneOf in the json schema:
     *
     * {@code "anyOf": [ { "type": "string" }, { "type": "number" } ] }
     *
     * When a json schema defines a combined schema, all of the properties in the combined schema
     * will be generated in the resulting POJO
     */
    COMBINED,

    /**
     * This property was defined as an enum in the json schema:
     *
     * {@code "enum": ["Street", "Avenue", "Boulevard"] }
     *
     * The resulting values will be generated as a java {@link Enum} class
     */
    ENUM,

    /**
     * This property was defined as an integer in the json schema:
     *
     * {@code { "type": "integer" } }
     *
     * The resulting field will be generated as an {@link Integer}
     */
    INTEGER,

    /**
     * This property was defined as a number in the json schema and supports floating point values:
     *
     * {@code { "type": "number" } }
     *
     * The resulting field will be generated as a {@link java.math.BigDecimal}
     */
    NUMBER,

    /**
     * This property was defined as a reference pointing to a reusable definition
     *
     * {@code { "$ref": "#/definitions/UUID" } }
     *
     * The resulting field will be generated depending on the type of the referenced definition
     */
    REFERENCE,

    /**
     * This refers to the containing root object of the schema and will be generated as a class
     */
    ROOT,

    /**
     * This property was defined as an string in the json schema:
     *
     * {@code { "type": "string" } }
     *
     * The resulting field will be generated as a {@link String}
     * 
     */
    STRING
}

package uk.gov.justice.generation.pojo.dom;

/**
 * Interface of all the field/class/enum/array definitions which make up the DOM
 */
public interface Definition {

    /**
     * Gets the name of the definition that will be used when the definition is a field
     * in a containing class
     *
     * @return The field name of the definition
     */
    String getFieldName();

    /**
     * Gets the {@link DefinitionType} of this definition
     * @return The {@link DefinitionType} of this definition
     */
    DefinitionType type();

    /**
     * Specifies whether the property in the JSON schema file that creates this
     * definition was marked as required. This is used to determine if a java
     * {@link java.util.Optional} should be used to wrap this field. By default
     * required is true.
     *
     * @return Whether this definition is required
     */
    boolean isRequired();

    /**
     * Sets whether this definition was specified as a required property in the JSON schema
     * file.
     *
     * @param required whether this field is required or not.
     */
    void setRequired(final boolean required);
}

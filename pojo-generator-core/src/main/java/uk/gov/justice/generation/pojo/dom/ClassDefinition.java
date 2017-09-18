package uk.gov.justice.generation.pojo.dom;

import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a class that will be generated as a java POJO
 */
public class ClassDefinition extends FieldDefinition {

    private final List<Definition> fieldDefinitions = new ArrayList<>();

    private boolean allowAdditionalProperties = false;
    private boolean root = false;

    /**
     * Creates a ClassDefinition that is not the root class.
     *
     * @param type The {@link DefinitionType} of the class. Can be a
     *              {@link DefinitionType#CLASS}
     *              {@link DefinitionType#ENUM} or
     *              {@link DefinitionType#COMBINED}
     * @param fieldName The name of the field that will be used in the generated POJO
     */
    public ClassDefinition(final DefinitionType type, final String fieldName) {
        super(type, fieldName);
    }

    /**
     * Adds a field to this class
     *
     * @param fieldDefinition The {@link Definition} of this field
     */
    public void addFieldDefinition(final Definition fieldDefinition) {
        fieldDefinitions.add(fieldDefinition);
    }

    /**
     * Gets the list of all fields defined for this class
     *
     * NB: The list of fields is sorted alphabetically by thier field names. This
     * results in all fields in both the constructor and the order of fields in the class
     * will be alphabetical.
     *
     * @return The list of all fields defined for this class
     */
    public List<Definition> getFieldDefinitions() {

        sortDefinitionsByFieldNameFirst();

        return unmodifiableList(fieldDefinitions);

    }

    /**
     * Determines whether 'additionalProperties' were allowed in the json schema definition
     * of this class. If additionalProperties' is true and the
     * {@link uk.gov.justice.generation.pojo.plugin.classmodifying.AddAdditionalPropertiesToClassPlugin}
     * is being used, then an extra hash map is included in the POJO to hold any extra unknown
     * properties in the json document
     *
     * @return Whether additionalProperties was set to true in the json schema
     */
    public boolean allowAdditionalProperties() {
        return allowAdditionalProperties;
    }

    /**
     * Sets whether 'additionalProperties' were allowed for this class
     *
     * @param allowAdditionalProperties whether 'additionalProperties' in the json schema
     *                                  file was true or false
     */
    public void setAllowAdditionalProperties(final boolean allowAdditionalProperties) {
        this.allowAdditionalProperties = allowAdditionalProperties;
    }

    /**
     * Determines whether this is the root definition.
     *
     * @return true if the root
     */
    public boolean isRoot() {
        return root;
    }

    /**
     * Sets whether this is the root defintion.
     *
     * @param root true if the root
     */
    public void setRoot(final boolean root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return "ClassDefinition{" +
                "fieldName=" + getFieldName() +
                ", type=" + type() +
                ", required=" + isRequired() +
                ", allowAdditionalProperties=" + allowAdditionalProperties +
                '}';
    }

    private void sortDefinitionsByFieldNameFirst() {
        fieldDefinitions.sort(comparing(Definition::getFieldName));
    }
}

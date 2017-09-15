package uk.gov.justice.generation.pojo.dom;

import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;


/**
 * Defines a reference in the json schema document. {@see {@link DefinitionType#REFERENCE}}
 */
public class ReferenceDefinition extends ClassDefinition {

    private final String referenceValue;

    public ReferenceDefinition(final String fieldName, final String referenceValue) {
        super(REFERENCE, fieldName);
        this.referenceValue = referenceValue;
    }

    public String getReferenceValue() {
        return referenceValue;
    }
}

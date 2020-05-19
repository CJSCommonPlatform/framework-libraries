package uk.gov.justice.generation.pojo.dom;

import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;

import uk.gov.justice.generation.pojo.visitor.ReferenceValue;


/**
 * Defines a reference in the json schema document. {@link DefinitionType#REFERENCE}
 */
public class ReferenceDefinition extends ClassDefinition {

    private final ReferenceValue referenceValue;

    public ReferenceDefinition(final String fieldName, final ReferenceValue referenceValue) {
        super(REFERENCE, fieldName);
        this.referenceValue = referenceValue;
    }

    public ReferenceValue getReferenceValue() {
        return referenceValue;
    }
}

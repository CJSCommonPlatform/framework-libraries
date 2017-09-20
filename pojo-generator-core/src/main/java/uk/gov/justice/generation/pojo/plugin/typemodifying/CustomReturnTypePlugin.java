package uk.gov.justice.generation.pojo.plugin.typemodifying;

import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;

import com.google.common.annotations.VisibleForTesting;
import com.squareup.javapoet.TypeName;

public class CustomReturnTypePlugin implements TypeModifyingPlugin {

    private final ReferenceToClassNameConverter referenceToClassNameConverter;

    public CustomReturnTypePlugin() {
        this(new ReferenceToClassNameConverter());
    }

    @VisibleForTesting // will fix this next story
    public CustomReturnTypePlugin(final ReferenceToClassNameConverter referenceToClassNameConverter) {
        this.referenceToClassNameConverter = referenceToClassNameConverter;
    }

    @Override
    public TypeName modifyTypeName(final TypeName typeName, final Definition definition) {

        if(REFERENCE.equals(definition.type())) {
            final ReferenceDefinition referenceDefinition = (ReferenceDefinition) definition;
            final String referenceValue = referenceDefinition.getReferenceValue();
            return referenceToClassNameConverter.get(referenceValue);
        }

        return typeName;
    }
}

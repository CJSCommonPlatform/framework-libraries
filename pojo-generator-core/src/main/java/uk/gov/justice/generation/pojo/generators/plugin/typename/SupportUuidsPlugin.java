package uk.gov.justice.generation.pojo.generators.plugin.typename;

import static com.squareup.javapoet.ClassName.get;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;

import java.util.UUID;

import com.squareup.javapoet.TypeName;

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

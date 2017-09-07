package uk.gov.justice.generation.pojo.generators.plugin.typename;

import static com.squareup.javapoet.TypeName.get;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;

import java.time.ZonedDateTime;

import com.squareup.javapoet.TypeName;

public class ZonedDateTimeTypeNamePlugin implements TypeNamePlugin {

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

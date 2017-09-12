package uk.gov.justice.generation.pojo.generators.plugin.typename;

import static com.squareup.javapoet.ClassName.get;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ARRAY;

import uk.gov.justice.generation.pojo.dom.Definition;

import java.util.Optional;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public class SupportJavaOptionalsPlugin implements TypeModifyingPlugin {

    @Override
    public TypeName modifyTypeName(final TypeName originalTypeName, final Definition definition) {

        if (shouldAddOptional(definition)) {
            return ParameterizedTypeName.get(get(Optional.class), originalTypeName);
        }

        return originalTypeName;
    }

    private boolean shouldAddOptional(final Definition definition) {
        return definition.type() != ARRAY && !definition.isRequired();
    }
}

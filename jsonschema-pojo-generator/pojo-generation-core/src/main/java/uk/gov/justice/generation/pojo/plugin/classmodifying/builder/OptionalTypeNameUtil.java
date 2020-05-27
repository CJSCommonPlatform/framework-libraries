package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import java.util.Objects;
import java.util.Optional;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public class OptionalTypeNameUtil {

    public TypeName getOptionalTypeFrom(final ParameterizedTypeName typeName) {
        return typeName.typeArguments.get(0);
    }

    public boolean isOptionalType(final TypeName typeName) {
        return typeName instanceof ParameterizedTypeName
                && Objects.equals(((ParameterizedTypeName) typeName).rawType.simpleName(), Optional.class.getSimpleName());
    }
}

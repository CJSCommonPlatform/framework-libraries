package uk.gov.justice.generation.pojo.plugin.typemodifying;

import uk.gov.justice.generation.pojo.core.TypeMapping;

import java.util.function.Predicate;

public final class TypeMappingPredicate {

    private TypeMappingPredicate() {
    }

    public static final Predicate<TypeMapping> REFERENCE_TYPE = typeMapping -> "reference".equals(typeMapping.getType());

    public static final Predicate<TypeMapping> FORMAT_TYPE = typeMapping -> "format".equals(typeMapping.getType());
}

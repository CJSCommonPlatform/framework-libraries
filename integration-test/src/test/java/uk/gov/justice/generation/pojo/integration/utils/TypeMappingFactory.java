package uk.gov.justice.generation.pojo.integration.utils;

import static uk.gov.justice.generation.pojo.integration.utils.PojoGeneratorPropertiesBuilder.setField;

import uk.gov.justice.generation.pojo.core.TypeMapping;

public class TypeMappingFactory {

    private static String type;
    private static String name;
    private static String implementation;

    public static TypeMapping typeMappingOf(final String type, final String name, final String implementation) throws IllegalAccessException {
        final TypeMapping typeMapping = new TypeMapping();

        setField(typeMapping, "type", type);
        setField(typeMapping, "name", name);
        setField(typeMapping, "implementation", implementation);

        return typeMapping;
    }

}

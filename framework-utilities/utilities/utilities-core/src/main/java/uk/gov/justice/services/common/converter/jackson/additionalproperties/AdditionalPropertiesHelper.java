package uk.gov.justice.services.common.converter.jackson.additionalproperties;

import java.util.function.Predicate;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;


public class AdditionalPropertiesHelper {

    private AdditionalPropertiesHelper() {
    }

    static final String ADDITIONAL_PROPERTIES_NAME = "additionalProperties";

    static final Predicate<BeanPropertyDefinition> hasAdditionalPropertiesName =
            beanPropertyDefinition -> beanPropertyDefinition.getFullName().getSimpleName().equals(ADDITIONAL_PROPERTIES_NAME);
}

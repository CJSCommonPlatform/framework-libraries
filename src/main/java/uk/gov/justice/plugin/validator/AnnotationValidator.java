package uk.gov.justice.plugin.validator;

import uk.gov.justice.plugin.domain.ValidationResult;
import uk.gov.justice.plugin.exception.ValidationException;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

public interface AnnotationValidator<T> {

    ValidationResult validate(final Class<?> classWithAnnotation, final Map<String, String> validationProperties) throws ValidationException;

    default String getApplicableAnnotationName() {
        return ((Class<?>) ((ParameterizedType) this.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0]).getName();
    }
}

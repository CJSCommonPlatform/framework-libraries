package uk.gov.justice.plugin.validator;

import uk.gov.justice.plugin.domain.ValidationResult;
import uk.gov.justice.plugin.exception.ValidationException;

import java.util.Map;

public interface AnnotationValidator {

    ValidationResult validate(final Class<?> classWithAnnotation, final Map<String, String> validationProperties) throws ValidationException;
}

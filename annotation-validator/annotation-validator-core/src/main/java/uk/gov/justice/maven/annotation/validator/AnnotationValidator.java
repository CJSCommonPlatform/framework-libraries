package uk.gov.justice.maven.annotation.validator;

import uk.gov.justice.maven.annotation.domain.ValidationResult;
import uk.gov.justice.maven.annotation.exception.ValidationException;

import java.util.Map;

/**
 * Interface for validator implementation of any object type.  Used specifically for validating
 * annotations
 *
 * @param <T> the type of Class this validator will support
 */
public interface AnnotationValidator<T> {

    ValidationResult validate(final Class<?> classWithAnnotation, final Map<String, String> validationProperties) throws ValidationException;

    Class<T> getApplicableAnnotationName();

}

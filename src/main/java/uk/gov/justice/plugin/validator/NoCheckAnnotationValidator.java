package uk.gov.justice.plugin.validator;

import uk.gov.justice.plugin.domain.ValidationResult;

import java.util.Map;

public class NoCheckAnnotationValidator implements AnnotationValidator {

    private static final String VALIDATION_TEXT = "";

    @Override
    public ValidationResult validate(final Class<?> classWithAnnotation, final Map<String, String> validationProperties) {
        return new ValidationResult(classWithAnnotation.getName(), true, VALIDATION_TEXT);
    }
}

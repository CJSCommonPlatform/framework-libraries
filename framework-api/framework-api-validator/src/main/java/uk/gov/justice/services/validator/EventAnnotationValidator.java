package uk.gov.justice.services.validator;

import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isBlank;

import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.maven.annotation.domain.ValidationResult;
import uk.gov.justice.maven.annotation.exception.ValidationException;
import uk.gov.justice.maven.annotation.validator.AnnotationValidator;

import java.util.Map;
import java.util.regex.Pattern;

public class EventAnnotationValidator implements AnnotationValidator<Event> {

    @Override
    public ValidationResult validate(final Class<?> classWithAnnotation, final Map<String, String> validationProperties) {

        final Event annotation = classWithAnnotation.getAnnotation(Event.class);

        final String serviceName = validationProperties.get("serviceName");
        if (isBlank(serviceName)) {
            throw new ValidationException("Service name should be supplied");
        }

        final String annotationValue = annotation.value();
        if (getEventPattern(serviceName).matcher(annotationValue).matches()) {
            return new ValidationResult(classWithAnnotation.getName(), true, annotationValue);
        }

        return new ValidationResult(classWithAnnotation.getName(), false, annotationValue);
    }

    @Override
    public Class<Event> getApplicableAnnotationName() {
        return Event.class;
    }

    private Pattern getEventPattern(final String serviceName) {
        // example matching pattern: service.events.something-happened
        // or: administration.events.something-happened
        return compile("^(administration|" + serviceName + ").events\\.[a-z]+.[a-z0-9\\-]+[a-z0-9]+$");
    }
}

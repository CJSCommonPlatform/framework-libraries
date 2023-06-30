package uk.gov.justice.services.validator;

import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import uk.gov.justice.maven.annotation.domain.ValidationResult;
import uk.gov.justice.maven.annotation.exception.ValidationException;
import uk.gov.justice.services.validator.domain.ClassWithInvalidEventAnnotation;
import uk.gov.justice.services.validator.domain.ClassWithValidAdministrationEventAnnotation;
import uk.gov.justice.services.validator.domain.ClassWithValidEventAnnotation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class EventAnnotationValidatorTest {

    private EventAnnotationValidator validator;

    @BeforeEach
    public void setup() {
        validator = new EventAnnotationValidator();
    }

    @Test
    public void shouldThrowExceptionIfServiceNameNotProvided() {
        assertThrows(ValidationException.class, () -> validator.validate(ClassWithValidEventAnnotation.class, of()));
    }

    @Test
    public void shouldIndicateValidationFailureWhenEventNameViolatesPattern() {
        final ValidationResult result = validator.validate(ClassWithInvalidEventAnnotation.class, of("serviceName", "service"));
        assertThat(result.getClassWithAnnotation(), is(ClassWithInvalidEventAnnotation.class.getName()));
        assertThat(result.getValidationText(), is("service.invalid"));
        assertThat(result.isValidationPassed(), is(false));
    }

    @Test
    public void shouldIndicateValidationSuccessWhenEventNameConformsToPattern() {
        final ValidationResult result = validator.validate(ClassWithValidEventAnnotation.class, of("serviceName", "service"));
        assertThat(result.getClassWithAnnotation(), is(ClassWithValidEventAnnotation.class.getName()));
        assertThat(result.getValidationText(), is("service.events.valid"));
        assertThat(result.isValidationPassed(), is(true));
    }

    @Test
    public void shouldIndicateValidationSuccessWhenEventNameStartsWithAdministrationInsteadOfServiceName() {
        final ValidationResult result = validator.validate(ClassWithValidAdministrationEventAnnotation.class, of("serviceName", "service"));
        assertThat(result.getClassWithAnnotation(), is(ClassWithValidAdministrationEventAnnotation.class.getName()));
        assertThat(result.getValidationText(), is("administration.events.valid"));
        assertThat(result.isValidationPassed(), is(true));
    }
}
package uk.gov.justice.plugin.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.plugin.validator.AnnotationValidatorFactory.getValidator;

import uk.gov.justice.maven.annotation.validator.AnnotationValidator;
import uk.gov.justice.plugin.domain.TestAnnotation;
import uk.gov.justice.plugin.exception.ValidatorNotFoundException;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AnnotationValidatorFactoryTest {

    private ClassLoader classLoader;

    @BeforeEach
    public void setup() {
        classLoader = getClass().getClassLoader();
    }

    @Test
    public void getValidatorForTestAnnotationType() {
        final AnnotationValidator validator = getValidator(classLoader, TestAnnotation.class.getName());
        assertThat(validator, instanceOf(TestAnnotationValidator.class));
    }

    @Test
    public void getValidatorForOtherTypes() {
        assertThrows(ValidatorNotFoundException.class, () -> getValidator(classLoader, Inject.class.getName()));
    }
}
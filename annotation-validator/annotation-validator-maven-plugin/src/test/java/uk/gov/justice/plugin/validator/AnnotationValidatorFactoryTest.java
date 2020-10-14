package uk.gov.justice.plugin.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static uk.gov.justice.plugin.validator.AnnotationValidatorFactory.getValidator;

import uk.gov.justice.maven.annotation.validator.AnnotationValidator;
import uk.gov.justice.plugin.domain.TestAnnotation;
import uk.gov.justice.plugin.exception.ValidatorNotFoundException;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

public class AnnotationValidatorFactoryTest {

    private ClassLoader classLoader;

    @Before
    public void setup() {
        classLoader = getClass().getClassLoader();
    }

    @Test
    public void getValidatorForTestAnnotationType() {
        final AnnotationValidator validator = getValidator(classLoader, TestAnnotation.class.getName());
        assertThat(validator, instanceOf(TestAnnotationValidator.class));
    }

    @Test(expected = ValidatorNotFoundException.class)
    public void getValidatorForOtherTypes() {
        getValidator(classLoader, Inject.class.getName());
    }
}
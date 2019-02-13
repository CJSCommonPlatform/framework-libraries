package uk.gov.justice.maven.annotation.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.maven.annotation.domain.ValidationResult;

import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class AnnotationValidatorTest {

    @Test
    public void getApplicableAnnotationName() {

        final Class<String> applicableAnnotationName = new AnnotationValidator<String>() {
            @Override
            public ValidationResult validate(final Class<?> classWithAnnotation, final Map<String, String> validationProperties) {
                return null;
            }

            @Override
            public Class<String> getApplicableAnnotationName() {
                return String.class;
            }
        }.getApplicableAnnotationName();
        assertThat(applicableAnnotationName.getName(), is(String.class.getName()));
    }
}
package uk.gov.justice.plugin.validator;

import static java.lang.Class.forName;
import static java.lang.String.format;

import uk.gov.justice.maven.annotation.validator.AnnotationValidator;
import uk.gov.justice.plugin.exception.ValidatorNotFoundException;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationValidatorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationValidatorFactory.class);

    public static AnnotationValidator getValidator(final ClassLoader classLoader, final String annotationClass) {

        final ClassInfoList classesImplementing = new ClassGraph().enableClassInfo().scan().getClassesImplementing(AnnotationValidator.class.getName());

        for (ClassInfo classInfo : classesImplementing) {
            try {
                final String implementingClassName = classInfo.getName();
                LOGGER.info("Processing class '{}'", implementingClassName);
                final Class<?> validatorClass = forName(implementingClassName, false, classLoader);
                final AnnotationValidator validatorImplementation = (AnnotationValidator) validatorClass.newInstance();
                if (StringUtils.equals(validatorImplementation.getApplicableAnnotationName().getName(), annotationClass)) {
                    return validatorImplementation;
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                LOGGER.error("Encountered error finding validator implementation for annotation class '" + annotationClass + "'.", e);
            }
        }

        throw new ValidatorNotFoundException(format("Unable to find validator implementation for annotation class '%s'", annotationClass));
    }
}

package uk.gov.justice.generation.pojo.core;

import uk.gov.justice.generation.pojo.dom.ClassName;

import java.time.ZonedDateTime;
import java.util.UUID;

public class ClassNameProvider {

    public ClassName classNameFor(final String description) {
        return classNameFor(description, String.class);
    }

    public ClassName classNameFor(final String description, final Class<?> defaultClass) {
        if(UUID.class.getSimpleName().equals(description)) {
            return new ClassName(UUID.class);
        }

        if(ZonedDateTime.class.getSimpleName().equals(description)) {
            return new ClassName(ZonedDateTime.class);
        }

        return new ClassName(defaultClass);
    }
}

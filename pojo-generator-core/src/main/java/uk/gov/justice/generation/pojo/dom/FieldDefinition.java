package uk.gov.justice.generation.pojo.dom;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import java.util.Optional;

public class FieldDefinition implements Definition {

    private final String fieldName;
    private final ClassName className;
    private final Optional<ClassName> genericType;

    public FieldDefinition(
            final String fieldName,
            final ClassName className) {
        this(fieldName, className, null);
    }

    public FieldDefinition(
            final String fieldName,
            final ClassName className,
            final ClassName genericType) {
        this.fieldName = fieldName;
        this.className = className;
        this.genericType = ofNullable(genericType);
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public ClassName getClassName() {
        return className;
    }

    @Override
    public Optional<ClassName> getGenericType() {
        return genericType;
    }
}

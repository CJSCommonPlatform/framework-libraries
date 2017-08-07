package uk.gov.justice.generation.pojo.dom;

import static java.util.Collections.unmodifiableList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClassDefinition implements Definition {

    private final ClassName className;
    private final String fieldName;
    private final Optional<ClassName> genericType;
    private final List<Definition> fieldDefinitions = new ArrayList<>();

    public ClassDefinition(final String fieldName, final ClassName className) {
        this(fieldName, className, null);
    }

    public ClassDefinition(final String fieldName, final ClassName className, final ClassName genericType) {
        this.className = className;
        this.fieldName = fieldName;
        this.genericType = ofNullable(genericType);
    }

    @Override
    public ClassName getClassName() {
        return className;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    public void addFieldDefinition(final Definition fieldDefinition) {
        fieldDefinitions.add(fieldDefinition);
    }

    public List<Definition> getFieldDefinitions() {
        return unmodifiableList(fieldDefinitions);
    }

    @Override
    public Optional<ClassName> getGenericType() {
        return genericType;
    }
}

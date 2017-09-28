package uk.gov.justice.generation.pojo.dom;

import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import java.util.Optional;

public class StringDefinition extends FieldDefinition {

    private final Optional<String> format;

    public StringDefinition(final String fieldName, final String format) {
        super(STRING, fieldName);
        this.format = Optional.of(format);
    }

    public StringDefinition(final String fieldName) {
        super(STRING, fieldName);
        this.format = Optional.empty();
    }

    public Optional<String> getFormat() {
        return format;
    }
}

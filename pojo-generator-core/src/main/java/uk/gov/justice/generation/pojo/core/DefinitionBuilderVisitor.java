package uk.gov.justice.generation.pojo.core;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.capitalize;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;

public class DefinitionBuilderVisitor implements Visitor {

    private static final String ARRAY_FIELD_NAME_SUFFIX = "List";

    private final Deque<Entry> definitions = new ArrayDeque<>();
    private final List<Definition> classDefinitions = new ArrayList<>();
    private final ClassNameProvider classNameProvider = new ClassNameProvider();
    private final String packageName;
    private final Optional<String> eventName;

    public DefinitionBuilderVisitor(final String packageName) {
        this.packageName = packageName;
        this.eventName = Optional.empty();
    }

    public DefinitionBuilderVisitor(final String packageName, final String eventName) {
        this.packageName = packageName;
        this.eventName = Optional.ofNullable(eventName);
    }

    @Override
    public void enter(final String fieldName, final Schema schema) {
        final ClassName className = new ClassName(packageName, capitalize(fieldName));

        final ClassDefinition definition;
        if (definitions.isEmpty() && eventName.isPresent()) {
            definition = new ClassDefinition(fieldName, className, eventName.get());
        } else {
            definition = new ClassDefinition(fieldName, className);
        }

        definitions.push(new Entry(schema, definition));
    }

    @Override
    public void leave(final Schema schema) {
        final Deque<Definition> fieldDefinitions = new ArrayDeque<>();

        while (definitions.peek().getSchema() != schema) {

            fieldDefinitions.push(definitions.pop().getDefinition());
        }

        final ClassDefinition classDefinition = (ClassDefinition) definitions.peek().getDefinition();
        fieldDefinitions.forEach(classDefinition::addFieldDefinition);
        classDefinitions.add(classDefinition);
    }

    @Override
    public void enter(final String fieldName, final ArraySchema schema) {
        final String className = capitalize(fieldName);
        final ClassName listClassName = new ClassName(List.class);
        final ClassName genericTypeName = new ClassName(packageName, className);
        final FieldDefinition definition = new FieldDefinition(fieldName + ARRAY_FIELD_NAME_SUFFIX, listClassName, genericTypeName);

        definitions.push(new Entry(schema, definition));
    }

    @Override
    public void leave(final ArraySchema schema) {
        while (definitions.peek().getSchema() != schema) {
            definitions.pop();
        }
    }

    @Override
    public void visit(final String fieldName, final StringSchema schema) {
        final ClassName className = classNameProvider.classNameFor(schema.getDescription());
        definitions.push(new Entry(schema, new FieldDefinition(fieldName, className)));
    }

    @Override
    public void visit(final String fieldName, final BooleanSchema schema) {
        definitions.push(new Entry(schema, new FieldDefinition(fieldName, new ClassName(Boolean.class))));
    }

    @Override
    public void visit(final String fieldName, final NumberSchema schema) {
        final ClassName className = schema.requiresInteger() ? new ClassName(Integer.class) : new ClassName(BigDecimal.class);
        definitions.push(new Entry(schema, new FieldDefinition(fieldName, className)));
    }

    @Override
    public void visit(final String fieldName, final EnumSchema schema) {
        final Set<Object> possibleValues = schema.getPossibleValues();
        final List<String> enumValues = possibleValues.stream().map(i -> (String) i).collect(toList());
        final EnumDefinition enumDefinition = new EnumDefinition(fieldName, new ClassName(packageName, capitalize(fieldName)), enumValues);
        definitions.push(new Entry(schema, enumDefinition));
        classDefinitions.add(enumDefinition);
    }

    @Override
    public List<Definition> getDefinitions() {
        return classDefinitions;
    }

    private class Entry {

        private final Schema schema;
        private final Definition definition;

        Entry(final Schema schema, final Definition definition) {
            this.schema = schema;
            this.definition = definition;
        }

        Schema getSchema() {
            return schema;
        }

        Definition getDefinition() {
            return definition;
        }
    }
}

package uk.gov.justice.generation.pojo.core;

import static org.apache.commons.lang.StringUtils.capitalize;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EmptySchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NullSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;

public class DefinitionBuilderVisitor implements Visitor {

    private final Deque<Entry> definitions = new ArrayDeque<>();
    private final List<ClassDefinition> classDefinitions = new ArrayList<>();
    private final ClassNameProvider classNameProvider = new ClassNameProvider();
    private final String packageName;

    public DefinitionBuilderVisitor(final String packageName) {
        this.packageName = packageName;
    }

    @Override
    public void enter(final String fieldName, final ObjectSchema schema) {
        final ClassDefinition definition = new ClassDefinition(fieldName, new ClassName(packageName, capitalize(fieldName)));

        definitions.push(new Entry(schema, definition));
    }

    @Override
    public void leave(final ObjectSchema schema) {
        final Deque<Definition> fieldDefinitions = new ArrayDeque<>();

        while (definitions.peek().getSchema() != schema) {

            fieldDefinitions.push(definitions.pop().getDefinition());
        }

        final ClassDefinition classDefinition = (ClassDefinition) definitions.peek().getDefinition();
        fieldDefinitions.forEach(classDefinition::addFieldDefinition);
        classDefinitions.add(classDefinition);
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
    public void visit(final String fieldName, final CombinedSchema schema) {
        //TODO: Implement Combined Schema
    }

    @Override
    public void visit(final String fieldName, final ArraySchema schema) {
        //TODO: Implement Array Schema
    }

    @Override
    public void visit(final String fieldName, final EmptySchema schema) {
        //TODO: Implement Empty Schema
    }

    @Override
    public void visit(final String fieldName, final EnumSchema schema) {
        //TODO: Implement Enum Schema
    }

    @Override
    public void visit(final String fieldName, final NullSchema schema) {
        //TODO: Implement Null Schema
    }

    @Override
    public List<ClassDefinition> getDefinitions() {
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

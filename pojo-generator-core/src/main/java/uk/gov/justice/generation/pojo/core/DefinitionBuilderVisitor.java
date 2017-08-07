package uk.gov.justice.generation.pojo.core;

import static org.apache.commons.lang.StringUtils.capitalize;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EmptySchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NullSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;

public class DefinitionBuilderVisitor implements Visitor {

    private final Deque<Entry> definitions = new LinkedList<>();
    private final List<ClassDefinition> classDefinitions = new ArrayList<>();
    private final String packageName;

    public DefinitionBuilderVisitor(final String packageName) {
        this.packageName = packageName;
    }

    @Override
    public void visitEnter(final ObjectSchema schema) {
        final String fieldName = schema.getId();
        final ClassDefinition definition = new ClassDefinition(fieldName, new ClassName(packageName, capitalize(fieldName)));

        definitions.push(new Entry(schema, definition));
    }

    @Override
    public void visitLeave(final ObjectSchema schema) {
        final Deque<Definition> tmpStack = new LinkedList<>();

        while (definitions.peek().getSchema() != schema) {
            tmpStack.push(definitions.pop().getDefinition());
        }

        final ClassDefinition classDefinition = (ClassDefinition) definitions.peek().getDefinition();

        while (!tmpStack.isEmpty()) {
            final Definition definition = tmpStack.pop();
            classDefinition.addFieldDefinition(definition);
        }

        classDefinitions.add(classDefinition);
    }

    @Override
    public void visit(final StringSchema schema) {
        definitions.push(new Entry(schema, new FieldDefinition(schema.getId(), new ClassName(String.class))));
    }

    @Override
    public void visit(final BooleanSchema schema) {
        definitions.push(new Entry(schema, new FieldDefinition(schema.getId(), new ClassName(Boolean.class))));
    }

    @Override
    public void visit(final NumberSchema schema) {
        final ClassName className = schema.requiresInteger() ? new ClassName(Integer.class) : new ClassName(BigDecimal.class);

        definitions.push(new Entry(schema, new FieldDefinition(schema.getId(), className)));
    }

    @Override
    public void visit(final CombinedSchema schema) {
        //TODO: Implement Combined Schema
    }

    @Override
    public void visit(final ArraySchema schema) {
        //TODO: Implement Array Schema
    }

    @Override
    public void visit(final ReferenceSchema schema) {
        //TODO: Implement Reference Schema
    }

    @Override
    public void visit(final EmptySchema schema) {
        //TODO: Implement Empty Schema
    }

    @Override
    public void visit(final EnumSchema schema) {
        //TODO: Implement Enum Schema
    }

    @Override
    public void visit(final NullSchema schema) {
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

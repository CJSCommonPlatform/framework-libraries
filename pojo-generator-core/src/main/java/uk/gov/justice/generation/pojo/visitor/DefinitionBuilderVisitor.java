package uk.gov.justice.generation.pojo.visitor;

import static com.google.common.collect.Lists.reverse;
import static java.util.Collections.emptyList;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;

public class DefinitionBuilderVisitor implements Visitor {

    private final Deque<Entry> definitions = new ArrayDeque<>();
    private final Deque<Entry> combinedDefinitions = new ArrayDeque<>();

    private final List<Definition> classDefinitions = new ArrayList<>();
    private final DefinitionFactory definitionFactory;

    public DefinitionBuilderVisitor(final DefinitionFactory definitionFactory) {
        this.definitionFactory = definitionFactory;
    }

    @Override
    public void enter(final String fieldName, final ObjectSchema schema) {

        final ClassDefinition definition;

        if (definitions.isEmpty()) {
            definition = (ClassDefinition) definitionFactory.constructRootClassDefinition(fieldName);
        } else {
            definition = (ClassDefinition) definitionFactory.constructDefinitionFor(fieldName, schema);
        }

        definition.setAllowAdditionalProperties(schema.permitsAdditionalProperties());
        addToClassDefinitionsIfNotPartOfCombinedDefinition(fieldName, definition);

        definitions.push(new Entry(fieldName, schema, definition));
    }

    @Override
    public void leave(final ObjectSchema schema) {
        addFieldDefinitionsFor(schema, schema.getRequiredProperties());
    }

    @Override
    public void enter(final String fieldName, final CombinedSchema schema) {
        final Definition definition = definitionFactory.constructDefinitionFor(fieldName, schema);

        addToClassDefinitionsIfNotPartOfCombinedDefinition(fieldName, definition);

        final Entry entry = new Entry(fieldName, schema, definition);
        definitions.push(entry);
        combinedDefinitions.push(entry);
    }

    @Override
    public void leave(final CombinedSchema schema) {
        addFieldDefinitionsFor(schema, emptyList());

        if (combinedDefinitions.peek().getSchema() == schema) {
            combinedDefinitions.pop();
        }
    }

    @Override
    public void enter(final String fieldName, final ArraySchema schema) {
        final Definition definition = definitionFactory.constructDefinitionFor(fieldName, schema);
        definitions.push(new Entry(fieldName, schema, definition));
    }

    @Override
    public void leave(final ArraySchema schema) {
        final Deque<Definition> fieldDefinitions = getChildDefinitionsFor(schema);

        final ClassDefinition classDefinition = (ClassDefinition) definitions.peek().getDefinition();
        fieldDefinitions.forEach(fieldDefinition -> {
            fieldDefinition.setRequired(true);
            classDefinition.addFieldDefinition(fieldDefinition);
        });
    }

    @Override
    public void enter(final String fieldName, final ReferenceSchema schema) {
        final Definition definition = definitionFactory.constructDefinitionFor(fieldName, schema);
        definitions.push(new Entry(fieldName, schema, definition));
    }

    @Override
    public void leave(final ReferenceSchema schema) {
        final Deque<Definition> fieldDefinitions = getChildDefinitionsFor(schema);

        final ClassDefinition classDefinition = (ClassDefinition) definitions.peek().getDefinition();
        fieldDefinitions.forEach(fieldDefinition -> {
            fieldDefinition.setRequired(true);
            classDefinition.addFieldDefinition(fieldDefinition);
        });
    }

    @Override
    public void visit(final String fieldName, final StringSchema schema) {
        final Definition definition = definitionFactory.constructDefinitionFor(fieldName, schema);
        definitions.push(new Entry(fieldName, schema, definition));
    }

    @Override
    public void visit(final String fieldName, final BooleanSchema schema) {
        final Definition definition = definitionFactory.constructDefinitionFor(fieldName, schema);
        definitions.push(new Entry(fieldName, schema, definition));
    }

    @Override
    public void visit(final String fieldName, final NumberSchema schema) {
        final Definition definition = definitionFactory.constructDefinitionFor(fieldName, schema);
        definitions.push(new Entry(fieldName, schema, definition));
    }

    @Override
    public void visit(final String fieldName, final EnumSchema schema) {
        final Definition definition = definitionFactory.constructDefinitionFor(fieldName, schema);
        definitions.push(new Entry(fieldName, schema, definition));
        classDefinitions.add(definition);
    }

    public List<Definition> getDefinitions() {
        return reverse(classDefinitions);
    }

    private void addToClassDefinitionsIfNotPartOfCombinedDefinition(final String fieldName, final Definition definition) {
        if (combinedDefinitions.isEmpty() || !combinedFieldNameEqualTo(fieldName)) {
            classDefinitions.add(definition);
        }
    }

    private boolean combinedFieldNameEqualTo(final String fieldName) {
        final String combinedFieldName = combinedDefinitions.peek().fieldName;

        return combinedFieldName.equals(fieldName);
    }

    private void addFieldDefinitionsFor(final Schema schema, final List<String> requiredFields) {
        final Deque<Definition> fieldDefinitions = getChildDefinitionsFor(schema);

        final ClassDefinition classDefinition = (ClassDefinition) definitions.peek().getDefinition();
        fieldDefinitions.forEach(fieldDefinition -> {
            fieldDefinition.setRequired(requiredFields.contains(fieldDefinition.getFieldName()));
            classDefinition.addFieldDefinition(fieldDefinition);
        });
    }

    private Deque<Definition> getChildDefinitionsFor(final Schema schema) {
        final Deque<Definition> fieldDefinitions = new ArrayDeque<>();

        while (definitions.peek().getSchema() != schema) {
            fieldDefinitions.push(definitions.pop().getDefinition());
        }

        return fieldDefinitions;
    }

    private class Entry {

        private final Schema schema;
        private final Definition definition;
        private final String fieldName;

        Entry(final String fieldName, final Schema schema, final Definition definition) {
            this.schema = schema;
            this.definition = definition;
            this.fieldName = fieldName;
        }

        Schema getSchema() {
            return schema;
        }

        Definition getDefinition() {
            return definition;
        }
    }
}

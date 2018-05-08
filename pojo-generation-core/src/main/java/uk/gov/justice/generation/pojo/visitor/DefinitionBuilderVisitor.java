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
import org.everit.json.schema.EmptySchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NullSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;

/**
 * This implementation of {@link Visitor} builds a {@link Definition} model from visiting a Schema.
 * It builds a Definition tree structure but also keeps track of Definitions that will be created as
 * Java POJOs.  These are returned at the end of the visitation by calling the getDefinitions
 * method.
 */
public class DefinitionBuilderVisitor implements Visitor {

    private final Deque<Entry> definitions = new ArrayDeque<>();
    private final Deque<Entry> combinedDefinitions = new ArrayDeque<>();

    private final List<Definition> classDefinitions = new ArrayList<>();
    private final DefinitionFactory definitionFactory;

    public DefinitionBuilderVisitor(final DefinitionFactory definitionFactory) {
        this.definitionFactory = definitionFactory;
    }

    /**
     * Enter an {@link ObjectSchema}.  Checks for root definition, adds definition to Java POJO list
     * and tracks definition using a Stack.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link ObjectSchema} to enter
     */
    @Override
    public void enter(final String fieldName, final ObjectSchema schema) {

        final ClassDefinition definition = createClassDefinition(fieldName, schema);

        definition.setAllowAdditionalProperties(schema.permitsAdditionalProperties());
        addToClassDefinitionsIfNotPartOfCombinedDefinition(fieldName, definition);

        definitions.push(new Entry(fieldName, schema, definition));
    }

    /**
     * Leave an {@link ObjectSchema}. Adds any child definitions to the definition.
     *
     * @param schema the {@link ObjectSchema} to leave
     */
    @Override
    public void leave(final ObjectSchema schema) {
        addFieldDefinitionsFor(schema, schema.getRequiredProperties());
    }

    /**
     * Enter a {@link CombinedSchema}.  Adds definitions to Java POJO list, track in definitions and
     * combinedDefinitions.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link CombinedSchema} to enter
     */
    @Override
    public void enter(final String fieldName, final CombinedSchema schema) {
        final ClassDefinition definition = createClassDefinition(fieldName, schema);

        addToClassDefinitionsIfNotPartOfCombinedDefinition(fieldName, definition);

        final Entry entry = new Entry(fieldName, schema, definition);
        definitions.push(entry);
        combinedDefinitions.push(entry);
    }

    /**
     * Leave a {@link CombinedSchema}.  Add any child definitions to the definition and remove from
     * combinedDefinition tracking.
     *
     * @param schema the {@link CombinedSchema} to leave
     */
    @Override
    public void leave(final CombinedSchema schema) {
        addFieldDefinitionsFor(schema, emptyList());
        combinedDefinitions.pop();
    }

    /**
     * Enter an {@link ArraySchema}.  Add definition to definitions tracking.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link ArraySchema} to enter
     */
    @Override
    public void enter(final String fieldName, final ArraySchema schema) {
        final Definition definition = definitionFactory.constructDefinitionFor(fieldName, schema);
        definitions.push(new Entry(fieldName, schema, definition));
    }

    /**
     * Leave an {@link ArraySchema}. Add any item definitions to the definition.
     *
     * @param schema the {@link ArraySchema} to leave
     */
    @Override
    public void leave(final ArraySchema schema) {
        final Deque<Definition> fieldDefinitions = getChildDefinitionsFor(schema);

        final ClassDefinition classDefinition = (ClassDefinition) definitions.peek().getDefinition();
        fieldDefinitions.forEach(fieldDefinition -> {
            fieldDefinition.setRequired(true);
            classDefinition.addFieldDefinition(fieldDefinition);
        });
    }

    /**
     * Enter a {@link ReferenceSchema}.  Add definition to definitions tracking.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link ReferenceSchema} to enter
     */
    @Override
    public void enter(final String fieldName, final ReferenceSchema schema) {
        final Definition definition = definitionFactory.constructDefinitionFor(fieldName, schema);
        definitions.push(new Entry(fieldName, schema, definition));
    }

    /**
     * Leave a {@link ReferenceSchema}.  Add any referred definitions to the definition.
     *
     * @param schema the {@link ReferenceSchema} to leave
     */
    @Override
    public void leave(final ReferenceSchema schema) {
        final Deque<Definition> fieldDefinitions = getChildDefinitionsFor(schema);

        final ClassDefinition classDefinition = (ClassDefinition) definitions.peek().getDefinition();
        fieldDefinitions.forEach(fieldDefinition -> {
            fieldDefinition.setRequired(true);
            classDefinition.addFieldDefinition(fieldDefinition);
        });
    }

    /**
     * Visit a {@link StringSchema}. Add definition to definitions tracking.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link StringSchema} to enter
     */
    @Override
    public void visit(final String fieldName, final StringSchema schema) {
        final Definition definition = definitionFactory.constructDefinitionFor(fieldName, schema);
        definitions.push(new Entry(fieldName, schema, definition));
    }

    /**
     * Visit a {@link BooleanSchema}. Add definition to definitions tracking.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link BooleanSchema} to enter
     */
    @Override
    public void visit(final String fieldName, final BooleanSchema schema) {
        final Definition definition = definitionFactory.constructDefinitionFor(fieldName, schema);
        definitions.push(new Entry(fieldName, schema, definition));
    }

    /**
     * Visit a {@link NumberSchema}. Add definition to definitions tracking.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link NumberSchema} to enter
     */
    @Override
    public void visit(final String fieldName, final NumberSchema schema) {
        final Definition definition = definitionFactory.constructDefinitionFor(fieldName, schema);
        definitions.push(new Entry(fieldName, schema, definition));
    }

    /**
     * Visit a {@link EnumSchema}. Add definition to definitions tracking.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link EnumSchema} to enter
     */
    @Override
    public void visit(final String fieldName, final EnumSchema schema) {
        final Definition definition = definitionFactory.constructDefinitionFor(fieldName, schema);
        definitions.push(new Entry(fieldName, schema, definition));
        classDefinitions.add(definition);
    }

    /**
     * Visit a {@link NullSchema}. Do nothing.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link NullSchema} to enter
     */
    @Override
    public void visit(final String fieldName, final NullSchema schema) {
        // do nothing
    }

    /**
     * Enter an {@link EmptySchema}.  Checks for root definition, adds definition to Java POJO list
     * and tracks definition using a Stack.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link EmptySchema} to enter
     */
    @Override
    public void visit(final String fieldName, final EmptySchema schema) {
        final ClassDefinition definition = createClassDefinition(fieldName, schema);

        definition.setAllowAdditionalProperties(true);
        addToClassDefinitionsIfNotPartOfCombinedDefinition(fieldName, definition);

        definitions.push(new Entry(fieldName, schema, definition));
    }

    /**
     * Returns the List of {@link Definition} that are generatable Java POJO classes.
     *
     * @return the List of {@link Definition}
     */
    public List<Definition> getDefinitions() {
        return reverse(classDefinitions);
    }

    private void addToClassDefinitionsIfNotPartOfCombinedDefinition(final String fieldName, final Definition definition) {
        if (combinedDefinitions.isEmpty() || !isCombinedFieldNameEqualTo(fieldName)) {
            classDefinitions.add(definition);
        }
    }

    /**
     * Compare the top of the combinedDefinitions stack fieldName with the given fieldName.
     *
     * @param fieldName the field name to compare with the top of the combined definitions stack
     * @return true if the field names match otherwise false
     */
    private boolean isCombinedFieldNameEqualTo(final String fieldName) {
        final String combinedFieldName = combinedDefinitions.peek().fieldName;

        return combinedFieldName.equals(fieldName);
    }

    private ClassDefinition createClassDefinition(final String fieldName, final Schema schema) {
        if (definitions.isEmpty()) {
            return (ClassDefinition) definitionFactory.constructRootDefinitionFor(fieldName, schema);
        }
        
        return (ClassDefinition) definitionFactory.constructDefinitionFor(fieldName, schema);
    }

    /**
     * Retrieve all child definition for a {@link Schema} then add them to the parent definition
     * while setting their required status.
     *
     * @param schema         the schema to get all child definitions for
     * @param requiredFields the List of required field names
     */
    private void addFieldDefinitionsFor(final Schema schema, final List<String> requiredFields) {
        final Deque<Definition> fieldDefinitions = getChildDefinitionsFor(schema);

        final ClassDefinition classDefinition = (ClassDefinition) definitions.peek().getDefinition();
        fieldDefinitions.forEach(fieldDefinition -> {
            fieldDefinition.setRequired(requiredFields.contains(fieldDefinition.getFieldName()));
            classDefinition.addFieldDefinition(fieldDefinition);
        });
    }

    /**
     * Retrieve all child definitions for a {@link Schema} as a Deque to keep original ordering.
     *
     * @param schema the schema to match
     * @return a Deque of child {@link Definition}
     */
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

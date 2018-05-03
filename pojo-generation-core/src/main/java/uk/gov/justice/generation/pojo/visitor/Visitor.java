package uk.gov.justice.generation.pojo.visitor;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EmptySchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NullSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.StringSchema;

/**
 * Defines a Visitor for visiting a Visitable.  The corresponding method is called when a visitable
 * visits each Schema type.  More for more complex Schemas the enter and leave methods are called,
 * allowing the tracking if child Schemas.
 *
 * The process to visit an ObjectSchema with a StringSchema child works as follows:
 *
 * enter(fieldName, objectSchema);
 * visit(fieldName, stringSchema);
 * leave(objectSchema);
 *
 * It is assumed that if required the schema objects are tracked by the implementation and thus can
 * build up the desired transformation model.
 */
public interface Visitor {

    /**
     * Enter an {@link ObjectSchema} with a given fieldName.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link ObjectSchema} to enter
     */
    void enter(final String fieldName, final ObjectSchema schema);

    /**
     * Leave an {@link ObjectSchema}
     *
     * @param schema the {@link ObjectSchema} to leave
     */
    void leave(final ObjectSchema schema);

    /**
     * Enter a {@link CombinedSchema} with a given fieldName.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link CombinedSchema} to enter
     */
    void enter(final String fieldName, final CombinedSchema schema);

    /**
     * Leave a {@link CombinedSchema}
     *
     * @param schema the {@link CombinedSchema} to leave
     */
    void leave(final CombinedSchema schema);

    /**
     * Enter an {@link ArraySchema} with a given fieldName.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link ArraySchema} to enter
     */
    void enter(final String fieldName, final ArraySchema schema);

    /**
     * Leave an {@link ArraySchema}
     *
     * @param schema the {@link ArraySchema} to leave
     */
    void leave(final ArraySchema schema);

    /**
     * Enter a {@link ReferenceSchema} with a given fieldName.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link ReferenceSchema} to enter
     */
    void enter(final String fieldName, final ReferenceSchema schema);

    /**
     * Leave a {@link ReferenceSchema}
     *
     * @param schema the {@link ReferenceSchema} to leave
     */
    void leave(final ReferenceSchema schema);

    /**
     * Enter a {@link BooleanSchema} with a given fieldName.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link BooleanSchema} to enter
     */
    void visit(final String fieldName, final BooleanSchema schema);

    /**
     * Enter an {@link EnumSchema} with a given fieldName.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link EnumSchema} to enter
     */
    void visit(final String fieldName, final EnumSchema schema);

    /**
     * Enter a {@link NumberSchema} with a given fieldName.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link NumberSchema} to enter
     */
    void visit(final String fieldName, final NumberSchema schema);

    /**
     * Enter a {@link StringSchema} with a given fieldName.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link StringSchema} to enter
     */
    void visit(final String fieldName, final StringSchema schema);

    /**
     * Enter a {@link NullSchema} with a given fieldName.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link NullSchema} to enter
     */
    void visit(final String fieldName, final NullSchema schema);

    /**
     * Enter a {@link EmptySchema} with a given fieldName.
     *
     * @param fieldName the field name of the Schema
     * @param schema    the {@link EmptySchema} to enter
     */
    void visit(final String fieldName, final EmptySchema schema);
}

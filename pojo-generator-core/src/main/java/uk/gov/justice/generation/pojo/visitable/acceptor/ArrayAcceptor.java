package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.Schema;

/**
 * The ArrayAcceptor accepts an {@link ArraySchema}, handles visiting the ArraySchema and any item
 * schemas.
 */
public class ArrayAcceptor implements Acceptable {

    private final AcceptorService acceptorService;

    public ArrayAcceptor(final AcceptorService acceptorService) {
        this.acceptorService = acceptorService;
    }

    /**
     * Accept {@link ArraySchema}, visit schema, then visit the all item schema or each
     * item schema in array using {@link AcceptorService} to visit each item schema.
     *
     * @param fieldName - the field name of the schema being visited
     * @param schema    - the {@link Schema} being visited
     * @param visitor   - the {@link Visitor} to visit the schema
     */
    @Override
    public void accept(final String fieldName, final Schema schema, final Visitor visitor) {
        final ArraySchema arraySchema = (ArraySchema) schema;

        final Schema allItemSchema = arraySchema.getAllItemSchema();

        visitor.enter(fieldName, arraySchema);

        if (allItemSchema != null) {
            acceptorService.visitSchema(fieldName, allItemSchema, visitor);
        } else {
            arraySchema
                    .getItemSchemas()
                    .forEach(itemSchema -> acceptorService.visitSchema(fieldName, itemSchema, visitor));
        }

        visitor.leave(arraySchema);
    }
}

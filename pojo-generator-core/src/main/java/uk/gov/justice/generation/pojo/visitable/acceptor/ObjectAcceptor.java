package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.Map;

import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;

/**
 * The ObjectAcceptor accepts a {@link ObjectSchema} and handles visiting the ObjectSchema.
 */
public class ObjectAcceptor implements Acceptable {

    private final AcceptorService acceptorService;

    public ObjectAcceptor(final AcceptorService acceptorService) {
        this.acceptorService = acceptorService;
    }

    /**
     * Accept {@link ObjectSchema}, visit schema and visit all child schemas using the {@link
     * AcceptorService}.
     *
     * @param fieldName - the field name of the schema being visited
     * @param schema    - the {@link Schema} being visited
     * @param visitor   - the {@link Visitor} to visit the schema
     */
    @Override
    public void accept(final String fieldName, final Schema schema, final Visitor visitor) {
        final ObjectSchema objectSchema = (ObjectSchema) schema;

        final Map<String, Schema> propertySchemas = objectSchema.getPropertySchemas();

        visitor.enter(fieldName, objectSchema);
        propertySchemas.forEach((childName, childSchema) -> acceptorService.visitSchema(childName, childSchema, visitor));
        visitor.leave(objectSchema);
    }
}

package uk.gov.justice.generation.pojo.validation;

import static org.everit.json.schema.CombinedSchema.ONE_CRITERION;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchemaValidatorVisitableTest {

    @Test
    public void shouldVisitAnObjectSchema() throws Exception {

        final ObjectSchema objectSchema = ObjectSchema.builder().build();
        final SchemaValidatorVisitable schemaValidatorVisitable = new SchemaValidatorVisitable(objectSchema);
        final ValidatingVisitor visitor = mock(ValidatingVisitor.class);

        schemaValidatorVisitable.accept(visitor);

        verify(visitor).visit(objectSchema);
    }
                                                               
    @Test
    public void shouldVisitAnEnumSchema() throws Exception {

        final EnumSchema enumSchema = EnumSchema.builder().build();
        final SchemaValidatorVisitable schemaValidatorVisitable = new SchemaValidatorVisitable(enumSchema);
        final ValidatingVisitor visitor = mock(ValidatingVisitor.class);

        schemaValidatorVisitable.accept(visitor);

        verify(visitor).visit(enumSchema);
    }

    @Test
    public void shouldVisitAnArraySchema() throws Exception {

        final ArraySchema arraySchema = ArraySchema.builder().build();
        final SchemaValidatorVisitable schemaValidatorVisitable = new SchemaValidatorVisitable(arraySchema);
        final ValidatingVisitor visitor = mock(ValidatingVisitor.class);

        schemaValidatorVisitable.accept(visitor);

        verify(visitor).visit(arraySchema);
    }

    @Test
    public void shouldVisitAReferenceSchema() throws Exception {

        final ReferenceSchema referenceSchema = ReferenceSchema.builder().build();
        final SchemaValidatorVisitable schemaValidatorVisitable = new SchemaValidatorVisitable(referenceSchema);
        final ValidatingVisitor visitor = mock(ValidatingVisitor.class);

        schemaValidatorVisitable.accept(visitor);

        verify(visitor).visit(referenceSchema);
    }

    @Test
    public void shouldVisitACombinedSchema() throws Exception {

        final CombinedSchema combinedSchema = CombinedSchema.builder().criterion(ONE_CRITERION).build();
        final SchemaValidatorVisitable schemaValidatorVisitable = new SchemaValidatorVisitable(combinedSchema);
        final ValidatingVisitor visitor = mock(ValidatingVisitor.class);

        schemaValidatorVisitable.accept(visitor);

        verify(visitor).visit(combinedSchema);
    }

    @Test
    public void shouldNotVisitANumberSchema() throws Exception {

        final NumberSchema numberSchema = NumberSchema.builder().build();
        final SchemaValidatorVisitable schemaValidatorVisitable = new SchemaValidatorVisitable(numberSchema);
        final ValidatingVisitor visitor = mock(ValidatingVisitor.class);

        schemaValidatorVisitable.accept(visitor);

        verifyZeroInteractions(visitor);
    }

    @Test
    public void shouldFailAndNotVisitAnEmptySchema() throws Exception {

        final EmptySchema emptySchema = EmptySchema.builder().build();
        final SchemaValidatorVisitable schemaValidatorVisitable = new SchemaValidatorVisitable(emptySchema);
        final ValidatingVisitor visitor = mock(ValidatingVisitor.class);

        try {
            schemaValidatorVisitable.accept(visitor);
            fail();
        } catch (final UnsupportedSchemaException expected) {
            assertThat(expected.getMessage(), is("Schema of type 'EmptySchema' is not supported."));
        }
    }

    @Test
    public void shouldFailAndNotVisitANullSchema() throws Exception {

        final NullSchema nullSchema = NullSchema.builder().build();
        final SchemaValidatorVisitable schemaValidatorVisitable = new SchemaValidatorVisitable(nullSchema);
        final ValidatingVisitor visitor = mock(ValidatingVisitor.class);

        try {
            schemaValidatorVisitable.accept(visitor);
            fail();
        } catch (final UnsupportedSchemaException expected) {
            assertThat(expected.getMessage(), is("Schema of type 'NullSchema' is not supported."));
        }
    }

    @Test
    public void shouldNotVisitAStringSchema() throws Exception {

        final StringSchema stringSchema = StringSchema.builder().build();
        final SchemaValidatorVisitable schemaValidatorVisitable = new SchemaValidatorVisitable(stringSchema);
        final ValidatingVisitor visitor = mock(ValidatingVisitor.class);

        schemaValidatorVisitable.accept(visitor);

        verifyZeroInteractions(visitor);
    }

    @Test
    public void shouldNotVisitABooleanSchema() throws Exception {

        final BooleanSchema booleanSchema = BooleanSchema.builder().build();
        final SchemaValidatorVisitable schemaValidatorVisitable = new SchemaValidatorVisitable(booleanSchema);
        final ValidatingVisitor visitor = mock(ValidatingVisitor.class);

        schemaValidatorVisitable.accept(visitor);

        verifyZeroInteractions(visitor);
    }
}

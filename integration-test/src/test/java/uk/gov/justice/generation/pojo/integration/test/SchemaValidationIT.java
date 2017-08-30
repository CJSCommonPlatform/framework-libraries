package uk.gov.justice.generation.pojo.integration.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;
import uk.gov.justice.generation.pojo.validation.SchemaValidatorVisitor;
import uk.gov.justice.generation.pojo.validation.Validator;
import uk.gov.justice.generation.pojo.visitable.VisitableSchema;
import uk.gov.justice.generation.pojo.visitable.VisitableSchemaFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.DefaultAcceptorFactory;

import java.io.File;

import org.everit.json.schema.Schema;
import org.junit.Test;

public class SchemaValidationIT {

    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final SchemaValidatorVisitor schemaValidatorVisitor = new SchemaValidatorVisitor(new Validator());

    @Test
    public void shouldFailValidationIfAnEnumContainsDiversTypesOfValues() throws Exception {
        final File jsonSchemaFile = new File("src/test/resources/invalid-schemas/invalid-enum.json");
        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);

        final VisitableSchema visitableSchema = new VisitableSchemaFactory().createWith(schema, new DefaultAcceptorFactory(new VisitableSchemaFactory()));

        try {
            visitableSchema.accept("", schemaValidatorVisitor);
            fail();
        } catch (final UnsupportedSchemaException expected) {
            assertThat(expected.getMessage(), is("Enums must have members of the same type. Found java.lang.String, java.lang.Boolean out of possible values [Mr, false, Ms, 23, Mrs]"));
        }
    }

    @Test
    public void shouldFailValidationIfAnArrayContainsDiversTypesOfValues() throws Exception {
        final File jsonSchemaFile = new File("src/test/resources/invalid-schemas/invalid-array.json");
        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);

        final VisitableSchema visitableSchema = new VisitableSchemaFactory().createWith(schema, new DefaultAcceptorFactory(new VisitableSchemaFactory()));

        try {
            visitableSchema.accept("", schemaValidatorVisitor);
            fail();
        } catch (final UnsupportedSchemaException expected) {
            assertThat(expected.getMessage(), is("Arrays must have members of the same type. Found org.everit.json.schema.NumberSchema, org.everit.json.schema.StringSchema"));
        }
    }
}

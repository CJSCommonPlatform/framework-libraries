package uk.gov.justice.generation.pojo.integration.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.io.files.loader.SchemaLoaderResolver;
import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;
import uk.gov.justice.generation.pojo.validation.SchemaValidatorVisitor;
import uk.gov.justice.generation.pojo.validation.Validator;
import uk.gov.justice.generation.pojo.visitable.Visitable;
import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.DefaultAcceptorService;
import uk.gov.justice.schema.catalog.CatalogObjectFactory;
import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.File;

import org.everit.json.schema.Schema;
import org.junit.Test;

public class SchemaValidationIT {

    private final CatalogObjectFactory catalogObjectFactory = new CatalogObjectFactory();

    private final SchemaLoaderResolver schemaLoaderResolver = new SchemaLoaderResolver(
            catalogObjectFactory.rawCatalog(),
            catalogObjectFactory.schemaClientFactory(),
            catalogObjectFactory.jsonStringToSchemaConverter());

    private final SchemaLoader schemaLoader = new SchemaLoader(schemaLoaderResolver);
    private final SchemaValidatorVisitor schemaValidatorVisitor = new SchemaValidatorVisitor(new Validator());

    @Test
    public void shouldFailValidationIfEnumContainsDifferentTypesOfValues() throws Exception {
        final File jsonSchemaFile = new ClasspathFileResource()
                .getFileFromClasspath("/invalid-schemas/invalid-enum.json");
        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);

        final Visitable visitableSchema = new VisitableFactory().createWith("fieldName", schema, new DefaultAcceptorService(new VisitableFactory()));

        try {
            visitableSchema.accept(schemaValidatorVisitor);
            fail();
        } catch (final UnsupportedSchemaException expected) {
            assertThat(expected.getMessage(), is("Enums must have members of the same type. Found java.lang.String, java.lang.Boolean out of possible values [Mr, false, Ms, 23, Mrs]"));
        }
    }

    @Test
    public void shouldFailValidationIfArrayContainsDifferentTypesOfValues() throws Exception {
        final File jsonSchemaFile = new ClasspathFileResource()
                .getFileFromClasspath("/invalid-schemas/invalid-array.json");
        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);

        final Visitable visitableSchema = new VisitableFactory().createWith("fieldName", schema, new DefaultAcceptorService(new VisitableFactory()));

        try {
            visitableSchema.accept(schemaValidatorVisitor);
            fail();
        } catch (final UnsupportedSchemaException expected) {
            assertThat(expected.getMessage(), is("Arrays must have members of the same type. Found org.everit.json.schema.NumberSchema, org.everit.json.schema.StringSchema"));
        }
    }
}

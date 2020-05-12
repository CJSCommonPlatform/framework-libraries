package uk.gov.justice.schema.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import uk.gov.justice.schema.catalog.SchemaCatalogResolver;

import java.io.InputStream;

import javax.inject.Inject;

import org.apache.openejb.jee.Application;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Module;
import org.everit.json.schema.Schema;
import org.everit.json.schema.SchemaException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ApplicationComposer.class)
public class SchemaCatalogResolverIT {

    @Inject
    private SchemaCatalogResolver schemaCatalogResolver;

    @Module
    @Classes(cdi = true, value = {
            SchemaCatalogResolverProducer.class
    })
    public WebApp war() {
        return new WebApp()
                .contextRoot("schema-catalog-resolver-test")
                .addServlet("SchemaCatalogResolverIT", Application.class.getName());
    }

    @Test
    public void shouldReturnResolvedSchema() throws Exception {
        try (final InputStream schemaFileStream = this.getClass().getResourceAsStream("/json/schema/context/person.json")) {
            final JSONObject schemaJsonObject = new JSONObject(new JSONTokener(schemaFileStream));

            final Schema schema = schemaCatalogResolver.loadSchema(schemaJsonObject);

            assertThat(schema.getId(), is("http://justice.gov.uk/context/person.json"));
            assertTrue(schema.definesProperty("correspondence_address"));
        }
    }

    @Test(expected = SchemaException.class)
    public void shouldReturnInvalidSchema() throws Exception {
        try (final InputStream schemaFileStream = this.getClass().getResourceAsStream("/json/schema/context/invalid_reference.json")) {
            final JSONObject schemaJsonObject = new JSONObject(new JSONTokener(schemaFileStream));

            schemaCatalogResolver.loadSchema(schemaJsonObject);
        }
    }
}

package uk.gov.justice.schema.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import javax.inject.Inject;

import org.apache.openejb.jee.Application;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Module;
import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ApplicationComposer.class)
public class SchemaServiceIT {

    @Inject
    private SchemaCatalogService schemaCatalogService;

    @Module
    @Classes(cdi = true, value = {
            CatalogProducer.class,
            SchemaCatalogService.class
    })
    public WebApp war() {
        return new WebApp()
                .contextRoot("core-test")
                .addServlet("TestApp", Application.class.getName());
    }

    @Test
    public void shouldReturnCatalogEntry() throws Exception {
        final Optional<Schema> schema = schemaCatalogService.findSchema("http://justice.gov.uk/context/person.json");

        assertThat(schema.isPresent(), is(true));
        assertThat(schema.get().getId(), is("http://justice.gov.uk/context/person.json"));
    }

    @Test
    public void shouldReturnOptionalEmptyForUnknownId() throws Exception {
        final Optional<Schema> schema = schemaCatalogService.findSchema("UnknownId");

        assertThat(schema.isPresent(), is(false));
    }
}

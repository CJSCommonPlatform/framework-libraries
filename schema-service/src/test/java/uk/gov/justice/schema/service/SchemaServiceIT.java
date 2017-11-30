package uk.gov.justice.schema.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.schema.catalog.CatalogLoader;
import uk.gov.justice.schema.catalog.CatalogToSchemaResolver;
import uk.gov.justice.schema.catalog.ClasspathCatalogLoader;
import uk.gov.justice.schema.catalog.FileContentsAsStringLoader;
import uk.gov.justice.schema.catalog.JsonSchemaFileLoader;
import uk.gov.justice.schema.catalog.JsonStringToSchemaConverter;
import uk.gov.justice.schema.catalog.SchemaResolver;
import uk.gov.justice.schema.catalog.SchemaResolverAndLoader;
import uk.gov.justice.schema.catalog.util.ClasspathResourceLoader;
import uk.gov.justice.schema.catalog.util.UriResolver;
import uk.gov.justice.schema.catalog.util.UrlConverter;
import uk.gov.justice.schema.client.SchemaClientFactory;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

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
            CatalogLoader.class,
            CatalogToSchemaResolver.class,
            ClasspathCatalogLoader.class,
            ClasspathResourceLoader.class,
            FileContentsAsStringLoader.class,
            JsonSchemaFileLoader.class,
            JsonStringToSchemaConverter.class,
            ObjectMapperProducer.class,
            SchemaCatalogService.class,
            SchemaClientFactory.class,
            SchemaResolver.class,
            SchemaResolverAndLoader.class,
            UrlConverter.class,
            UriResolver.class,
            LoggerProducer.class
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

package uk.gov.justice.schema.catalog;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

public class CatalogIT {

    private final Catalog catalog = new CatalogObjectFactory().catalog();

    @Test
    public void shouldMapSchemasOnClasspathToTheirIds() throws Exception {

        final String id_1 = "http://justice.gov.uk/standards/address.json";
        final String json_1 = catalog.getSchema(id_1).get().toString();

        with(json_1)
                .assertThat("$.id", is(id_1))
                .assertThat("$.type", is("object"))
                .assertThat("$.properties.city.type", is("string"))
                .assertThat("$.properties.postcode.type", is("string"))
                .assertThat("$.properties.addressline1.type", is("string"))
                .assertThat("$.properties.addressline2.type", is("string"))
        ;

        final String id_2 = "http://justice.gov.uk/context/person.json";
        final String json_2 = catalog.getSchema(id_2).get().toString();

        with(json_2)
                .assertThat("$.id", is(id_2))
                .assertThat("$.type", is("object"))
                .assertThat("$.properties.correspondence_address.$ref", is("http://justice.gov.uk/standards/complex_address.json#/definitions/complex_address2"))
                .assertThat("$.properties.name.type", is("string"))
                .assertThat("$.properties.nino.type", is("string"))
        ;

        final String id_3 = "http://justice.gov.uk/standards/complex_address.json";
        final String json_3 = catalog.getSchema(id_3).get().toString();

        with(json_3)
                .assertThat("$.id", is(id_3))
                .assertThat("$.allOf[0].type", is("object"))
                .assertThat("$.allOf[1].allOf[0].$ref", is("#/definitions/complex_address"))
        ;
    }
}

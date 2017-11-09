package uk.gov.justice;

import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;
import uk.gov.justice.services.core.json.AbstractTestHelper;

import org.junit.Test;

import uk.gov.justice.schema.catalog.pojo.Catalog;
import uk.gov.justice.schema.catalog.pojo.CatalogWrapper;


//@RunWith(CDITestRunner)
public class JsonToPojoTest {

    private static final String JSON_EXAMPLES_VALIDATE_DIR = "/";

    @Test
    public void testSimpleCatalogLoad() throws Exception {
        String jsonString = AbstractTestHelper.getFileContents(JSON_EXAMPLES_VALIDATE_DIR + "catalog_test_valid.json");
//        JsonObjectToObjectConverter jsonConverter = new JsonObjectToObjectConverter();
//       // JsonObject j = new JsonObject()
//        final JSONObject jsonObject = new JSONObject(jsonString);
//        jsonConverter.convert((JsonObject) jsonObject, Catalog.class);

//        ObjectMapper objectMapper = new ObjectMapper();
//        Catalog catalog = objectMapper.readValue(jsonString, Catalog.class);

        ObjectMapperProducer objectMapperProducer = new ObjectMapperProducer();
        Catalog catalog = objectMapperProducer.objectMapper().readValue(jsonString, CatalogWrapper.class).getCatalog();

        System.out.println(catalog.getName());
        System.out.println(catalog.getGroup().get(0).getName());


    }


}

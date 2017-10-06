package uk.gov.justice;

import static junit.framework.TestCase.fail;

import uk.gov.justice.services.core.json.AbstractTestHelper;
import uk.gov.justice.services.core.json.DefaultJsonSchemaValidatorFactoryPI;
import uk.gov.justice.services.core.json.JsonSchemaValidator;

import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.junit.Test;

public class FrameworkJSONSchemaValidatorTest {

    private static final String JSON_TEST_FILE_DIR = "json/examples-framework/";
    JsonSchemaValidator frameworkValidator = DefaultJsonSchemaValidatorFactoryPI.getDefaultJsonSchemaValidator();



    @Test
    public void testValidationAddress_normal_case() throws Exception {
      String jsonString = AbstractTestHelper.getFileContents(JSON_TEST_FILE_DIR + "validAddress.json");
        System.out.println(jsonString);
        frameworkValidator.validate(jsonString, "address");
    }


    @Test
    public void testValidationAddress_failsvalidation_case() throws Exception {
         String jsonString = AbstractTestHelper.getFileContents(JSON_TEST_FILE_DIR + "invalidAddress.json");
        System.out.println(jsonString);
        try {
            frameworkValidator.validate(jsonString, "address");
            fail("test should have thrown a validation error");
        } catch (ValidationException e) {
            //fine for this
        }

    }


    @Test
    public void testValidationComplexAddress_normal_case() throws Exception {
        String jsonString = AbstractTestHelper.getFileContents(JSON_TEST_FILE_DIR + "validComplexAddress.json");
        System.out.println(jsonString);
        frameworkValidator.validate(jsonString, "complex_address");
    }


    @Test
    public void testValidationComplexAddress_failsvalidation_case() throws Exception {
         String jsonString = AbstractTestHelper.getFileContents(JSON_TEST_FILE_DIR + "invalidComplexAddress.json");
        System.out.println(jsonString);
        try {
            frameworkValidator.validate(jsonString, "complex_address");
            fail("test should have thrown a validation error");
        } catch (ValidationException e) {
            //fine for this
        }
    }


    @Test
    public void testValidationAddress_malformed_case() throws Exception {
      String jsonString = AbstractTestHelper.getFileContents(JSON_TEST_FILE_DIR + "malformedAddress.json");
        System.out.println(jsonString);
        try {
            frameworkValidator.validate(jsonString, "address");
            fail("test should have thrown a validation error");
        } catch (JSONException e) {
            //malformed json throws JSONException
            //fine for this
        }
    }



//    @Test
//    public void testValidationPerson_normal_case() throws Exception {
//
//        CPPSchemaClient client = new CPPSchemaClient();
//        client.loadSchemas("/json/schema/complex_address.json");
//       String jsonString = AbstractTestHelper.getFileContents(JSON_TEST_FILE_DIR + "validPerson.json");
//        System.out.println(jsonString);
//        frameworkValidator.validate(jsonString, "person");
//    }


    @Test
    public void testValidationPerson_failsvalidation_case() throws Exception {
       String jsonString = AbstractTestHelper.getFileContents(JSON_TEST_FILE_DIR + "invalidPerson.json");
        System.out.println(jsonString);
        try {
            frameworkValidator.validate(jsonString, "person");
            fail("test should have thrown a validation error");
        } catch (Exception e) {

        }

    }


}

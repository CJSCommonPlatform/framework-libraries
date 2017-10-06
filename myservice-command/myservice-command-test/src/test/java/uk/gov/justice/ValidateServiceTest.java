package uk.gov.justice;

import static junit.framework.TestCase.fail;

import uk.gov.justice.services.core.json.DefaultJsonSchemaValidatorFactoryPI;
import uk.gov.justice.services.core.json.JsonSchemaValidator;

import java.io.File;

import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.junit.Test;

public class ValidateServiceTest {

    private static final String JSON_EXAMPLES_VALIDATE_DIR = "json/examples-validateservice/";
    ValidateService validateService = new ValidateService();



    @Test
    public void testValidationAddress_normal_case() throws Exception {
      String jsonString = AbstractTestHelper.getFileContents(JSON_EXAMPLES_VALIDATE_DIR + "validAddress.json");
        System.out.println(jsonString);
        validateService.validate(jsonString, "address");
    }


    @Test
    public void testValidationAddress_failsvalidation_case() throws Exception {
         String jsonString = AbstractTestHelper.getFileContents(JSON_EXAMPLES_VALIDATE_DIR + "invalidAddress.json");
        System.out.println(jsonString);
        try {
            validateService.validate(jsonString, "address");
            fail("test should have thrown a validation error");
        } catch (ValidationException e) {
            //fine for this
        }

    }


    @Test
    public void testValidationComplexAddress_normal_case() throws Exception {
        String jsonString = AbstractTestHelper.getFileContents(JSON_EXAMPLES_VALIDATE_DIR + "validComplexAddress.json");
        System.out.println(jsonString);
        validateService.validate(jsonString, "complex_address");
    }


    @Test
    public void testValidationComplexAddress_failsvalidation_case() throws Exception {
         String jsonString = AbstractTestHelper.getFileContents(JSON_EXAMPLES_VALIDATE_DIR + "invalidComplexAddress.json");
        System.out.println(jsonString);
        try {
            validateService.validate(jsonString, "complex_address");
            fail("test should have thrown a validation error");
        } catch (ValidationException e) {
            //fine for this
        }
    }


    @Test
    public void testValidationAddress_malformed_case() throws Exception {
      String jsonString = AbstractTestHelper.getFileContents(JSON_EXAMPLES_VALIDATE_DIR + "malformedAddress.json");
        System.out.println(jsonString);
        try {
            validateService.validate(jsonString, "address");
            fail("test should have thrown a validation error");
        } catch (JSONException e) {
            //malformed json throws JSONException
            //fine for this
        }
    }



    @Test
    public void testValidationPerson_normal_case() throws Exception {
        CPPSchemaClient client = new CPPSchemaClient();
        client.loadSchemas("/json/schema/complex_address.json");
       String jsonString = AbstractTestHelper.getFileContents(JSON_EXAMPLES_VALIDATE_DIR + "validPerson.json");
        System.out.println(jsonString);
        validateService.validate(jsonString, "person");
    }


    @Test
    public void testValidationPerson_failsvalidation_case() throws Exception {
       String jsonString = AbstractTestHelper.getFileContents(JSON_EXAMPLES_VALIDATE_DIR + "invalidPerson.json");
        System.out.println(jsonString);
        try {
            validateService.validate(jsonString, "person");
            fail("test should have thrown a validation error");
        } catch (Exception e) {

        }

    }

}

package uk.gov.justice;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.raml.model.MimeType;
import org.raml.model.Raml;
import org.raml.parser.visitor.RamlDocumentBuilder;
import static org.junit.Assert.*;

public class ServiceDefinitionTest {

    private static final String MIMETYPE = "application/vnd.progression.command.add-case-to-crown-court+json";

    @Test
    public void testGetSchemaFromRAML() throws Exception {
        Raml raml =  new RamlDocumentBuilder().build("myservice_command_api.raml");

        Map<String, MimeType> mimes = raml.getResource("/cases/{caseId}").getAction("post").getBody();

        String schema = mimes.get(MIMETYPE).getSchema();
        Object compiledSchemaObj = mimes.get(MIMETYPE).getCompiledSchema();

        System.out.println(schema);
        System.out.println(compiledSchemaObj);


        assertNotNull(schema);
        assertNotNull(compiledSchemaObj);

        assertEquals(compiledSchemaObj.getClass(), String.class);




    }

}

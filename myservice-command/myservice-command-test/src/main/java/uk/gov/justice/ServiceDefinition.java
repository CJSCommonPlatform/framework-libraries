package uk.gov.justice;

import java.util.Map;

import org.raml.model.MimeType;
import org.raml.model.Raml;
import org.raml.parser.visitor.RamlDocumentBuilder;

public class ServiceDefinition {

    public static void main(String[] args) {
        Raml raml =  new RamlDocumentBuilder().build("raml/myservice_command_api.raml");

        Map<String, MimeType> mimes = raml.getResource("/cases/{caseId}").getAction("post").getBody();

        System.out.println(mimes.get("application/vnd.progression.command.add-case-to-crown-court+json").getSchema());

        System.out.println(mimes.get("application/vnd.progression.command.add-case-to-crown-court+json").getCompiledSchema());

        System.out.println(raml.getResource("/cases/{caseId}").getAction("post").getBody());

        for (Map<String, String> map: raml.getSchemas())
        {
            System.out.println(map);
        }
    }
}

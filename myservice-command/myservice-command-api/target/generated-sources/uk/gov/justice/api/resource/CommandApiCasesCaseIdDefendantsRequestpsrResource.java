package uk.gov.justice.api.resource;

import java.lang.String;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("cases/{caseId}/defendants/requestpsr")
public interface CommandApiCasesCaseIdDefendantsRequestpsrResource {
  @POST
  @Consumes("application/vnd.progression.command.request-psr-for-defendants+json")
  Response postProgressionCommandRequestPsrForDefendantsCasesByCaseIdDefendantsRequestpsr(@PathParam("caseId") String caseId, JsonObject entity);
}

package uk.gov.justice.api.resource;

import java.lang.String;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("cases/{caseId}/defendants/{defendantId}")
public interface CommandApiCasesCaseIdDefendantsDefendantIdResource {
  @POST
  @Consumes("application/vnd.progression.command.add-defendant-additional-information+json")
  Response postProgressionCommandAddDefendantAdditionalInformationCasesByCaseIdDefendantsByDefendantId(@PathParam("caseId") String caseId, @PathParam("defendantId") String defendantId, JsonObject entity);

  @POST
  @Consumes("application/vnd.progression.command.no-more-information-required+json")
  Response postProgressionCommandNoMoreInformationRequiredCasesByCaseIdDefendantsByDefendantId(@PathParam("caseId") String caseId, @PathParam("defendantId") String defendantId, JsonObject entity);
}

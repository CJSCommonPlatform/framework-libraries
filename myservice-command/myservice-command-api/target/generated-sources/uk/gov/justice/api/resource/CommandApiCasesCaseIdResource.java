package uk.gov.justice.api.resource;

import java.lang.String;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("cases/{caseId}")
public interface CommandApiCasesCaseIdResource {
  @POST
  @Consumes("application/vnd.progression.command.add-case-to-crown-court+json")
  Response postProgressionCommandAddCaseToCrownCourtCasesByCaseId(@PathParam("caseId") String caseId, JsonObject entity);

  @POST
  @Consumes("application/vnd.progression.command.add-sentence-hearing+json")
  Response postProgressionCommandAddSentenceHearingCasesByCaseId(@PathParam("caseId") String caseId, JsonObject entity);

  @POST
  @Consumes("application/vnd.progression.command.case-assigned-for-review+json")
  Response postProgressionCommandCaseAssignedForReviewCasesByCaseId(@PathParam("caseId") String caseId, JsonObject entity);

  @POST
  @Consumes("application/vnd.progression.command.case-to-be-assigned+json")
  Response postProgressionCommandCaseToBeAssignedCasesByCaseId(@PathParam("caseId") String caseId, JsonObject entity);

  @POST
  @Consumes("application/vnd.progression.command.prepare-for-sentence-hearing+json")
  Response postProgressionCommandPrepareForSentenceHearingCasesByCaseId(@PathParam("caseId") String caseId, JsonObject entity);

  @POST
  @Consumes("application/vnd.progression.command.sending-committal-hearing-information+json")
  Response postProgressionCommandSendingCommittalHearingInformationCasesByCaseId(@PathParam("caseId") String caseId, JsonObject entity);

  @POST
  @Consumes("application/vnd.progression.command.sentence-hearing-date+json")
  Response postProgressionCommandSentenceHearingDateCasesByCaseId(@PathParam("caseId") String caseId, JsonObject entity);
}

package uk.gov.justice.api.mapper;

import java.lang.Override;
import java.lang.String;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.HttpHeaders;
import uk.gov.justice.services.adapter.rest.mapping.ActionMapper;
import uk.gov.justice.services.adapter.rest.mapping.ActionMapperHelper;

@Named("DefaultCommandApiCasesCaseIdResourceActionMapper")
public class DefaultCommandApiCasesCaseIdResourceActionMapper implements ActionMapper {
  private ActionMapperHelper actionMapperHelper;

  @Inject
  public DefaultCommandApiCasesCaseIdResourceActionMapper(final ActionMapperHelper actionMapperHelper) {
    this.actionMapperHelper = actionMapperHelper;
    actionMapperHelper.add("postProgressionCommandSentenceHearingDateCasesByCaseId", "application/vnd.progression.command.sentence-hearing-date+json", "progression.command.sentence-hearing-date");
    actionMapperHelper.add("postProgressionCommandAddCaseToCrownCourtCasesByCaseId", "application/vnd.progression.command.add-case-to-crown-court+json", "progression.command.add-case-to-crown-court");
    actionMapperHelper.add("postProgressionCommandSendingCommittalHearingInformationCasesByCaseId", "application/vnd.progression.command.sending-committal-hearing-information+json", "progression.command.sending-committal-hearing-information");
    actionMapperHelper.add("postProgressionCommandCaseToBeAssignedCasesByCaseId", "application/vnd.progression.command.case-to-be-assigned+json", "progression.command.case-to-be-assigned");
    actionMapperHelper.add("postProgressionCommandCaseAssignedForReviewCasesByCaseId", "application/vnd.progression.command.case-assigned-for-review+json", "progression.command.case-assigned-for-review");
    actionMapperHelper.add("postProgressionCommandPrepareForSentenceHearingCasesByCaseId", "application/vnd.progression.command.prepare-for-sentence-hearing+json", "progression.command.prepare-for-sentence-hearing");
    actionMapperHelper.add("postProgressionCommandAddSentenceHearingCasesByCaseId", "application/vnd.progression.command.add-sentence-hearing+json", "progression.command.add-sentence-hearing");
  }

  @Override
  public String actionOf(final String methodName, final String httpMethod, final HttpHeaders headers) {
    return actionMapperHelper.actionOf(methodName, httpMethod, headers);
  }
}

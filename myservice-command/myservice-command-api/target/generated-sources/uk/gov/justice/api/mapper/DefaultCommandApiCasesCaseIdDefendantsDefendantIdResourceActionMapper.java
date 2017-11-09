package uk.gov.justice.api.mapper;

import java.lang.Override;
import java.lang.String;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.HttpHeaders;
import uk.gov.justice.services.adapter.rest.mapping.ActionMapper;
import uk.gov.justice.services.adapter.rest.mapping.ActionMapperHelper;

@Named("DefaultCommandApiCasesCaseIdDefendantsDefendantIdResourceActionMapper")
public class DefaultCommandApiCasesCaseIdDefendantsDefendantIdResourceActionMapper implements ActionMapper {
  private ActionMapperHelper actionMapperHelper;

  @Inject
  public DefaultCommandApiCasesCaseIdDefendantsDefendantIdResourceActionMapper(final ActionMapperHelper actionMapperHelper) {
    this.actionMapperHelper = actionMapperHelper;
    actionMapperHelper.add("postProgressionCommandAddDefendantAdditionalInformationCasesByCaseIdDefendantsByDefendantId", "application/vnd.progression.command.add-defendant-additional-information+json", "progression.command.add-defendant-additional-information");
    actionMapperHelper.add("postProgressionCommandNoMoreInformationRequiredCasesByCaseIdDefendantsByDefendantId", "application/vnd.progression.command.no-more-information-required+json", "progression.command.no-more-information-required");
  }

  @Override
  public String actionOf(final String methodName, final String httpMethod, final HttpHeaders headers) {
    return actionMapperHelper.actionOf(methodName, httpMethod, headers);
  }
}

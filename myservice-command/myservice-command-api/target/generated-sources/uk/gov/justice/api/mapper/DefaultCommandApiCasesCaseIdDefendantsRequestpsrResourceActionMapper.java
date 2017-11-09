package uk.gov.justice.api.mapper;

import java.lang.Override;
import java.lang.String;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.HttpHeaders;
import uk.gov.justice.services.adapter.rest.mapping.ActionMapper;
import uk.gov.justice.services.adapter.rest.mapping.ActionMapperHelper;

@Named("DefaultCommandApiCasesCaseIdDefendantsRequestpsrResourceActionMapper")
public class DefaultCommandApiCasesCaseIdDefendantsRequestpsrResourceActionMapper implements ActionMapper {
  private ActionMapperHelper actionMapperHelper;

  @Inject
  public DefaultCommandApiCasesCaseIdDefendantsRequestpsrResourceActionMapper(final ActionMapperHelper actionMapperHelper) {
    this.actionMapperHelper = actionMapperHelper;
    actionMapperHelper.add("postProgressionCommandRequestPsrForDefendantsCasesByCaseIdDefendantsRequestpsr", "application/vnd.progression.command.request-psr-for-defendants+json", "progression.command.request-psr-for-defendants");
  }

  @Override
  public String actionOf(final String methodName, final String httpMethod, final HttpHeaders headers) {
    return actionMapperHelper.actionOf(methodName, httpMethod, headers);
  }
}

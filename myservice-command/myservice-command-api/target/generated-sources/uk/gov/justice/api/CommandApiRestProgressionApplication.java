package uk.gov.justice.api;

import java.lang.Class;
import java.lang.Override;
import java.util.Set;
import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import uk.gov.justice.api.resource.DefaultCommandApiCasesCaseIdDefendantsDefendantIdResource;
import uk.gov.justice.api.resource.DefaultCommandApiCasesCaseIdDefendantsRequestpsrResource;
import uk.gov.justice.api.resource.DefaultCommandApiCasesCaseIdResource;
import uk.gov.justice.services.adapter.rest.application.CommonProviders;

@ApplicationPath("/command/api/rest/progression")
public class CommandApiRestProgressionApplication extends Application {
  @Inject
  CommonProviders commonProviders;

  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> classes = commonProviders.providers();
    classes.add(DefaultCommandApiCasesCaseIdResource.class);
    classes.add(DefaultCommandApiCasesCaseIdDefendantsDefendantIdResource.class);
    classes.add(DefaultCommandApiCasesCaseIdDefendantsRequestpsrResource.class);
    return classes;
  }
}

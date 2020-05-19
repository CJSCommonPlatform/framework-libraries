package uk.gov.justice.services.fileservice.it.helpers;

import uk.gov.justice.services.fileservice.repository.DataSourceProvider;

public class IntegrationTestDataSourceProvider extends DataSourceProvider {

    @Override
    protected String getJndiName() {
        return "java:openejb/Resource/DS.fileservice";
    }
}

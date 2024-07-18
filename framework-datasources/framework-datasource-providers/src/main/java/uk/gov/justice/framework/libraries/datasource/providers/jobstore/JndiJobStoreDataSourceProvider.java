package uk.gov.justice.framework.libraries.datasource.providers.jobstore;

import static java.lang.String.format;

import uk.gov.justice.framework.libraries.datasource.providers.DatasourceAcquisitionException;
import uk.gov.justice.services.common.configuration.JndiBasedServiceContextNameProvider;
import uk.gov.justice.services.jdbc.persistence.InitialContextFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.sql.DataSource;

@ApplicationScoped
public class JndiJobStoreDataSourceProvider implements JobStoreDataSourceProvider {

    private static final String JNDI_DS_JOB_STORE_PATTERN = "java:/app/%s/DS.jobstore";

    @Inject
    private JndiBasedServiceContextNameProvider jndiBasedServiceContextNameProvider;

    @Inject
    private InitialContextFactory initialContextFactory;

    private DataSource datasource = null;

    @Override
    public DataSource getJobStoreDataSource() {
        if (datasource == null) {
            final String serviceContextName = jndiBasedServiceContextNameProvider.getServiceContextName();
            final String jndiName = format(JNDI_DS_JOB_STORE_PATTERN, serviceContextName);
            try {
                datasource = (DataSource) initialContextFactory.create().lookup(jndiName);
            } catch (final NamingException e) {
                throw new DatasourceAcquisitionException(format("Failed to get jobstore datasource using jndi name '%s'", jndiName), e);
            }
        }
        
        return datasource;
    }
}

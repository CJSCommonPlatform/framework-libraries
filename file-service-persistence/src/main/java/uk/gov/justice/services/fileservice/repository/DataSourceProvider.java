package uk.gov.justice.services.fileservice.repository;

import static java.lang.String.format;

import uk.gov.justice.services.fileservice.api.ConfigurationException;
import uk.gov.justice.services.fileservice.api.FileServiceException;
import uk.gov.justice.services.jdbc.persistence.InitialContextFactory;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Gets the container {@link DataSource} using JNDI
 */
@Default
public class DataSourceProvider {

    private static final String JNDI_DATASOURCE = "java:/DS.fileservice";

    @Inject
    InitialContextFactory initialContextFactory;

    /**
     * Gets the container {@link DataSource} using JNDI
     *
     * @return A {@link DataSource} to the database
     */
    public DataSource getDatasource() throws FileServiceException {

        final String jndiName = getJndiName();

        try {
            return (DataSource) initialContextFactory.create().lookup(jndiName);
        } catch (final NamingException e) {
            throw new ConfigurationException(format("Failed to get Connection from container using JNDI name '%s'", jndiName), e);
        }
    }

    /**
     * Gets the JNDI name used to look up the container {@link DataSource}. Extracted as a separate
     * method to allow it to be overridden in the integration tests.
     * @return the File Service Datasource
     */
    protected String getJndiName() {
        return JNDI_DATASOURCE;
    }
}

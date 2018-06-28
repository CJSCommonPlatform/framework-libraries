package uk.gov.moj.cpp.jobstore.persistence;

import static java.lang.String.format;

import uk.gov.justice.services.jdbc.persistence.JdbcRepositoryException;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class JdbcJobStoreDataSourceProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcJobStoreDataSourceProvider.class);

    private static final String JNDI_DS_JOB_STORE_PATTERN = "java:/app/%s/DS.jobstore";

    private DataSource datasource = null;

    @Resource(lookup = "java:app/AppName")
    String warFileName;

    Context initialContext;

    JdbcJobStoreDataSourceProvider(final String warFileName, final Context initialContext) {
        this.warFileName = warFileName;
        this.initialContext = initialContext;
    }

    public JdbcJobStoreDataSourceProvider() throws NamingException {
        initialContext = getInitialContext();
    }

    public Context getInitialContext() throws NamingException {
        if (initialContext == null) {
            initialContext = new InitialContext();
        }
        return initialContext;
    }

    public DataSource getDataSource() {
        if (datasource == null) {
            try {
                datasource = (DataSource) getInitialContext().lookup(jndiName());
            } catch (final NamingException e) {
                throw new JdbcRepositoryException(e);
            }
        }
        return datasource;
    }

    private String jndiName() {
        final String name = format(JNDI_DS_JOB_STORE_PATTERN, warFileName);
        LOGGER.debug("Looking up JNDI resource for {}", name);
        return name;
    }
}

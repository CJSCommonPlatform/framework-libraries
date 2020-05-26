package uk.gov.moj.cpp.jobstore.persistence;

import static java.lang.String.format;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;

@ApplicationScoped
public class JdbcJobStoreDataSourceProvider {

    private static final String JNDI_DS_JOB_STORE_PATTERN = "java:/app/%s/DS.jobstore";

    @Inject
    private Logger logger;

    @Inject
    private Context initialContext;

    @Resource(lookup = "java:app/AppName")
    private String warFileName;

    private DataSource datasource = null;

    public DataSource getDataSource() {
        if (datasource == null) {
            try {
                datasource = (DataSource) initialContext.lookup(jndiName());
            } catch (final NamingException e) {
                throw new JdbcRepositoryException(e);
            }
        }
        return datasource;
    }

    private String jndiName() {
        final String name = format(JNDI_DS_JOB_STORE_PATTERN, warFileName);
        logger.debug("Looking up JNDI resource for {}", name);
        return name;
    }
}

package uk.gov.moj.cpp.jobstore.persistence;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.Context.URL_PKG_PREFIXES;
import static uk.gov.justice.services.test.utils.common.host.TestHostProvider.getHost;

import javax.sql.DataSource;

import org.apache.openejb.resource.jdbc.dbcp.BasicDataSource;
import org.postgresql.Driver;

public class PostgresDataSourceFactory {

    private static final String EVENT_STORE_URL = "jdbc:postgresql://" + getHost() + ":5432/frameworkjobstore";
    private static final String EVENT_STORE_USER_NAME = "framework";
    private static final String EVENT_STORE_PASSWORD = "framework";

    public DataSource createJobStoreDataSource() {

        final BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setJdbcDriver(Driver.class.getName());
        basicDataSource.setJdbcUrl(EVENT_STORE_URL);
        basicDataSource.setUserName(EVENT_STORE_USER_NAME);
        basicDataSource.setPassword(EVENT_STORE_PASSWORD);

        System.setProperty(INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        System.setProperty(URL_PKG_PREFIXES, "org.apache.naming");

        return basicDataSource;
    }
}

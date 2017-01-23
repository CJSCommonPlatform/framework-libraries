package uk.gov.justice.services.fileservice.it.helpers;

import java.sql.Connection;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

public class LiquibaseDatabaseBootstrapper {

    private static final String AN_EMPTY_STRING = "";

    public void bootstrap(final String liquibaseDbChangelogPath, final Connection connection) throws LiquibaseException {
        final Liquibase liquibase = new Liquibase(
                liquibaseDbChangelogPath,
                new ClassLoaderResourceAccessor(),
                new JdbcConnection(connection));
        liquibase.dropAll();
        liquibase.update(AN_EMPTY_STRING);
    }
}

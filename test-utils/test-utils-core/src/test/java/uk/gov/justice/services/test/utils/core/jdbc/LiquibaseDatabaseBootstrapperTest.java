package uk.gov.justice.services.test.utils.core.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.services.test.utils.core.jdbc.H2ConnectionParameters.DRIVER_CLASS;
import static uk.gov.justice.services.test.utils.core.jdbc.H2ConnectionParameters.PASSWORD;
import static uk.gov.justice.services.test.utils.core.jdbc.H2ConnectionParameters.URL;
import static uk.gov.justice.services.test.utils.core.jdbc.H2ConnectionParameters.USERNAME;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

public class LiquibaseDatabaseBootstrapperTest {

    private static final String LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML = "test-liquibase-scripts/liquibase-bootstrap-test-changelog.xml";
    private static final String SQL = "SELECT * FROM databasechangelog";

    private final LiquibaseDatabaseBootstrapper liquibaseDatabaseBootstrapper = new LiquibaseDatabaseBootstrapper();
    private final JdbcConnectionProvider jdbcConnectionProvider = new JdbcConnectionProvider();

    @Test
    public void shouldBootstrapTheDatabaseUsingLiquibase() throws Exception {

        try(final Connection connection = jdbcConnectionProvider.getConnection(
                URL,
                USERNAME,
                PASSWORD,
                DRIVER_CLASS)) {

            liquibaseDatabaseBootstrapper.bootstrap(
                    LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML,
                    connection);


            try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
                try(final ResultSet resultSet = preparedStatement.executeQuery()) {

                    assertThat(resultSet.next(), is(true));
                    assertThat(resultSet.getString("id"), is("001-liquibase-bootstrap-test.changelog"));

                    assertThat(resultSet.next(), is(true));
                    assertThat(resultSet.getString("id"), is("002-liquibase-bootstrap-test.changelog"));

                    assertThat(resultSet.next(), is(true));
                    assertThat(resultSet.getString("id"), is("003-liquibase-bootstrap-test.changelog"));

                    assertThat(resultSet.next(), is(false));
                }
            }
        }
    }
}

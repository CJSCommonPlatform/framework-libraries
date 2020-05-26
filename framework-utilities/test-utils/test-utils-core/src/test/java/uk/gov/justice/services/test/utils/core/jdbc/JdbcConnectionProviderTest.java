package uk.gov.justice.services.test.utils.core.jdbc;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static uk.gov.justice.services.test.utils.core.jdbc.H2ConnectionParameters.DRIVER_CLASS;
import static uk.gov.justice.services.test.utils.core.jdbc.H2ConnectionParameters.PASSWORD;
import static uk.gov.justice.services.test.utils.core.jdbc.H2ConnectionParameters.URL;
import static uk.gov.justice.services.test.utils.core.jdbc.H2ConnectionParameters.USERNAME;

import java.sql.Connection;

import org.junit.Test;

public class JdbcConnectionProviderTest {

    private JdbcConnectionProvider jdbcConnectionProvider = new JdbcConnectionProvider();

    @Test
    public void shouldCreateAnOpenJdbcConnection() throws Exception {

        try(final Connection connection = jdbcConnectionProvider.getConnection(
                URL,
                USERNAME,
                PASSWORD,
                DRIVER_CLASS)) {
            assertThat(connection, is(notNullValue()));
            assertThat(connection.isClosed(), is(false));
        }
    }

    @Test
    public void shouldTestTheExceptionHandlingToImproveTheTestCoverage() throws Exception {

        try {
            jdbcConnectionProvider.getConnection("url", "username", "password", "not a driver class name");
            fail();
        } catch (final RuntimeException expected) {
            assertThat(expected.getCause(), is(instanceOf(ClassNotFoundException.class)));
            assertThat(expected.getMessage(), is("Failed to get JDBC connection. url: 'url', username 'username', password 'password'"));
        }
    }
}

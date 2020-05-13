package uk.gov.justice.services.test.utils.core.jdbc;

import static java.lang.String.format;
import static java.sql.DriverManager.registerDriver;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Utility class for getting a hard JDBC {@link Connection}
 */
public class JdbcConnectionProvider {

    /**
     * Gets a JDBC {@link Connection} specified by the supplied connection parameters
     *
     * @param url the JDBC url to the database
     * @param username the database user name
     * @param password the database password
     * @param driverClassName the class name of the database driver you are using
     *
     * @return an open {@link Connection} to the database
     */
    public Connection getConnection(final String url, final String username, final String password, final String driverClassName) {
        try {
            final java.sql.Driver driver = (java.sql.Driver) Class.forName(driverClassName).newInstance();
            registerDriver(driver);
            return DriverManager.getConnection(url, username, password);
        } catch (final Exception e) {
            final String message = format("Failed to get JDBC connection. url: '%s', username '%s', password '%s'",
                    url,
                    username,
                    password);

            throw new RuntimeException(message, e);
        }
    }
}

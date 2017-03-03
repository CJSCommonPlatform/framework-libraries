package uk.gov.justice.services.fileservice.utils.test;

import static java.lang.String.format;
import static java.sql.DriverManager.registerDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionProvider {

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

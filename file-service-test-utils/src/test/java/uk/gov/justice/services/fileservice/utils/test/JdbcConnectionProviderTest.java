package uk.gov.justice.services.fileservice.utils.test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class JdbcConnectionProviderTest {

    private final JdbcConnectionProvider jdbcConnectionProvider = new JdbcConnectionProvider();

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

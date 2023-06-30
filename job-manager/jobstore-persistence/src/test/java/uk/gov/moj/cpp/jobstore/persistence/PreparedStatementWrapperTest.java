package uk.gov.moj.cpp.jobstore.persistence;

import static java.time.LocalDateTime.now;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PreparedStatementWrapperTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Test
    public void shouldCloseConnectionWhenPreparedStatementCreationCausesException() throws SQLException {

        final String query = "someQuery";

        when(connection.prepareStatement(query)).thenThrow(new SQLException());

        try {
            PreparedStatementWrapper.valueOf(connection, query);
        } catch (Exception e) {
            //Do nothing
        }

        verify(connection).close();
    }

    @Test
    public void shouldCloseConnectionAndStatementWhenQueryExecutionThrowsException() throws SQLException {

        final String query = "someQuery2";

        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenThrow(new SQLException());

        final PreparedStatementWrapper ps = PreparedStatementWrapper.valueOf(connection, query);

        try {
            ps.executeQuery();
        } catch (Exception e) {
            //Do nothing
        }

        final InOrder inOrder = inOrder(preparedStatement, connection);
        inOrder.verify(preparedStatement).close();
        inOrder.verify(connection).close();
    }

    @Test
    public void shouldCloseConnectionStatementAndResultset() throws SQLException {

        final String query = "someQuery3";

        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        final PreparedStatementWrapper ps = PreparedStatementWrapper.valueOf(connection, query);

        ps.executeQuery();
        ps.close();

        final InOrder inOrder = inOrder(resultSet, preparedStatement, connection);

        inOrder.verify(resultSet).close();
        inOrder.verify(preparedStatement).close();
        inOrder.verify(connection).close();
    }

    @Test
    public void shouldDelegateSetObjectMethodCall() throws SQLException {

        final String query = "dummy";
        final int parameterIndex = 1;
        final Object objectValue = new Object();

        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        final PreparedStatementWrapper ps = PreparedStatementWrapper.valueOf(connection, query);
        ps.setObject(parameterIndex, objectValue);

        verify(preparedStatement).setObject(parameterIndex, objectValue);
    }

    @Test
    public void shouldDelegateSetStringMethodCall() throws SQLException {

        final String query = "dummy";
        final int parameterIndex = 2;
        final String stringValue = "aaa";

        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        final PreparedStatementWrapper ps = PreparedStatementWrapper.valueOf(connection, query);
        ps.setString(parameterIndex, stringValue);

        verify(preparedStatement).setString(parameterIndex, stringValue);
    }

    @Test
    public void shouldDelegateSetLongMethodCall() throws SQLException {

        final String query = "dummy";
        final int parameterIndex = 2;
        final long longValue = 123l;

        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        final PreparedStatementWrapper ps = PreparedStatementWrapper.valueOf(connection, query);
        ps.setLong(parameterIndex, longValue);

        verify(preparedStatement).setLong(parameterIndex, longValue);
    }

    @Test
    public void shouldDelegateSetIntMethodCall() throws SQLException {

        final String query = "dummy";
        final int parameterIndex = 2;
        final int integerValue = 123;

        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        final PreparedStatementWrapper ps = PreparedStatementWrapper.valueOf(connection, query);
        ps.setInt(parameterIndex, integerValue);

        verify(preparedStatement).setInt(parameterIndex, integerValue);
    }

    @Test
    public void shouldDelegateSetBooleanMethodCall() throws SQLException {

        final String query = "dummy";
        final int parameterIndex = 2;
        final boolean booleanValue = false;

        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        final PreparedStatementWrapper ps = PreparedStatementWrapper.valueOf(connection, query);
        ps.setBoolean(parameterIndex, booleanValue);

        verify(preparedStatement).setBoolean(parameterIndex, booleanValue);
    }

    @Test
    public void shouldDelegateSetTimestampMethodCall() throws SQLException {

        final String query = "dummy";
        final int parameterIndex = 2;
        final Timestamp timestamp = Timestamp.valueOf(now());

        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        final PreparedStatementWrapper ps = PreparedStatementWrapper.valueOf(connection, query);
        ps.setTimestamp(parameterIndex, timestamp);

        verify(preparedStatement).setTimestamp(parameterIndex, timestamp);
    }

    @Test
    public void shouldDelegateExecuteUpdateMethodCall() throws SQLException {

        final String query = "someQuery3";

        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(4);

        final PreparedStatementWrapper ps = PreparedStatementWrapper.valueOf(connection, query);

        assertThat(ps.executeUpdate(), is(4));
    }

    @Test
    public void shouldCloseStatementAndConnectionOnExceptionOnSetObject() throws SQLException {

        final String query = "dummy";
        final int parameterIndex = 1;
        final Object objectValue = new Object();

        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        doThrow(new SQLException()).when(preparedStatement).setObject(parameterIndex, objectValue);

        final PreparedStatementWrapper ps = PreparedStatementWrapper.valueOf(connection, query);
        try {
            ps.setObject(parameterIndex, objectValue);
        } catch (Exception e) {
            //Do nothing
        }

        final InOrder inOrder = inOrder(preparedStatement, connection);
        inOrder.verify(preparedStatement).close();
        inOrder.verify(connection).close();
    }

    @Test
    public void shouldCloseStatementOnExceptionOnSetString() throws SQLException {

        final String query = "dummy";
        final int parameterIndex = 1;
        final String stringValue = "aaa";

        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        doThrow(new SQLException()).when(preparedStatement).setString(parameterIndex, stringValue);

        final PreparedStatementWrapper ps = PreparedStatementWrapper.valueOf(connection, query);
        try {
            ps.setString(parameterIndex, stringValue);
        } catch (Exception e) {
            //Do nothing
        }

        final InOrder inOrder = inOrder(preparedStatement, connection);
        inOrder.verify(preparedStatement).close();
        inOrder.verify(connection).close();
    }

    @Test
    public void shouldCloseStatementOnExceptionOnSetLong() throws SQLException {

        final String query = "dummy";
        final int parameterIndex = 1;
        final long longValue = 5L;

        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        doThrow(new SQLException()).when(preparedStatement).setLong(parameterIndex, longValue);

        final PreparedStatementWrapper ps = PreparedStatementWrapper.valueOf(connection, query);
        try {
            ps.setLong(parameterIndex, longValue);
        } catch (Exception e) {
            //Do nothing
        }

        final InOrder inOrder = inOrder(preparedStatement, connection);
        inOrder.verify(preparedStatement).close();
        inOrder.verify(connection).close();
    }
}

package uk.gov.moj.cpp.jobstore.persistence;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class JdbcResultSetStreamerTest {

    private final JdbcResultSetStreamer jdbcResultSetStreamer = new JdbcResultSetStreamer();

    @Test
    public void shouldConvertJdbcResultSetToAStreamOfObjects() throws Exception {

        final PreparedStatementWrapper preparedStatementWrapper = mock(PreparedStatementWrapper.class);
        final ResultSet resultSet = mock(ResultSet.class);

        when(preparedStatementWrapper.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("name")).thenReturn("Fred", "Bert");
        when(resultSet.getInt("age")).thenReturn(63, 59);

        final Function<ResultSet, Student> resultSetFunction = rs -> {

            try {
                return new Student(
                        rs.getString("name"),
                        rs.getInt("age"));

            } catch (final SQLException e) {
                throw new RuntimeException(e);
            }
        };

        try (final Stream<Student> studentStream = jdbcResultSetStreamer.streamOf(preparedStatementWrapper, resultSetFunction)) {

            final List<Student> students = studentStream.collect(toList());
            assertThat(students.size(), is(2));

            assertThat(students.get(0).getName(), is("Fred"));
            assertThat(students.get(0).getAge(), is(63));
            assertThat(students.get(1).getName(), is("Bert"));
            assertThat(students.get(1).getAge(), is(59));
        }

        verify(preparedStatementWrapper).close();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void shouldAlwaysCloseThePreparedStatmentOnException() throws Exception {

        final NullPointerException nullPointerException = new NullPointerException("Ooops");

        final PreparedStatementWrapper preparedStatementWrapper = mock(PreparedStatementWrapper.class);
        final ResultSet resultSet = mock(ResultSet.class);

        when(preparedStatementWrapper.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);

        final Stream<Student> studentStream = jdbcResultSetStreamer.streamOf(preparedStatementWrapper, rs -> {
            throw nullPointerException;
        });

        final JdbcRepositoryException expected = assertThrows(JdbcRepositoryException.class, () -> studentStream.collect(toList()));
        assertThat(expected.getCause(), is(nullPointerException));

        verify(preparedStatementWrapper).close();
    }

    private static class Student {
        private final String name;
        private final int age;

        private Student(final String name, final int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}

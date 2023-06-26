package uk.gov.moj.cpp.jobstore.persistence;

import static uk.gov.moj.cpp.jobstore.persistence.PreparedStatementWrapper.valueOf;

import java.sql.SQLException;

import javax.sql.DataSource;

public class PreparedStatementWrapperFactory {

    public PreparedStatementWrapper preparedStatementWrapperOf(final DataSource dataSource, final String query) throws SQLException {
        return valueOf(dataSource.getConnection(), query);
    }
}

package uk.gov.moj.cpp.jobmanager.example.util;

import uk.gov.justice.services.jdbc.persistence.JdbcRepositoryException;
import uk.gov.justice.services.jdbc.persistence.JdbcRepositoryHelper;
import uk.gov.justice.services.jdbc.persistence.PreparedStatementWrapper;
import uk.gov.justice.services.test.utils.core.messaging.Poller;
import uk.gov.moj.cpp.jobstore.persistence.JobJdbcRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class OpenEjbJobJdbcRepository extends JobJdbcRepository {

     private JdbcRepositoryHelper jdbcRepositoryHelper = new JdbcRepositoryHelper();


    public int getJobRecordCount() {
        int jobsCount = 0;
        try {
            final PreparedStatementWrapper ps = jdbcRepositoryHelper.preparedStatementWrapperOf(dataSource, "SELECT COUNT(*) FROM job");
            final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jobsCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new JdbcRepositoryException("Exception while retrieving jobs count", e);
        }
        return jobsCount;
    }



    public void waitForAllJobsToBeProcessed() {

        final Poller poller = new Poller(100, 500);

        poller.pollUntilFound(() -> {
            if (getJobRecordCount() == 0) {
                return Optional.of(true);
            }
            return Optional.empty();
        });

    }

}
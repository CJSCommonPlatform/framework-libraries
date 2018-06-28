package uk.gov.moj.cpp.jobstore.persistence;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class AnsiSQLJobDataSqlProviderTest {
    private static final String INSERT_JOB_SQL = "INSERT INTO job(job_id,worker_id,worker_lock_time,next_task,next_task_start_time,job_data) values (?,?,?,?,?,?)";
    private static final String UPDATE_JOB_DATA_SQL = "UPDATE job SET job_data = ? WHERE job_id = ?";

    @Test
    public void shouldReturnAnsiInsertSQL() {

        final AnsiJobSqlProvider ansiSQLJobDataSqlProvider = new AnsiJobSqlProvider();
        assertThat(ansiSQLJobDataSqlProvider.getInsertSql(), is(INSERT_JOB_SQL));
    }

    @Test
    public void shouldReturnAnsiUpdateJobDataSQL() {

        final AnsiJobSqlProvider ansiSQLJobDataSqlProvider = new AnsiJobSqlProvider();
        assertThat(ansiSQLJobDataSqlProvider.getUpdateJobDataSql(), is(UPDATE_JOB_DATA_SQL));
    }
}
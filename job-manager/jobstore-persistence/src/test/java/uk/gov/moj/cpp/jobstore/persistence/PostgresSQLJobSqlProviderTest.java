package uk.gov.moj.cpp.jobstore.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;

public class PostgresSQLJobSqlProviderTest {
    private static final String INSERT_JOB_SQL = "INSERT INTO job(job_id,worker_id,worker_lock_time,next_task,next_task_start_time,job_data) values (?,?,?,?,?,to_jsonb(?::json))";
    private static final String UPDATE_JOB_DATA_SQL = "UPDATE job SET job_data = to_jsonb(?::json) WHERE job_id = ?";

    @Test
    public void shouldReturnPostgresInsertSQL() {

        final PostgresJobSqlProvider postgresSQLJobSqlProvider = new PostgresJobSqlProvider();
        assertThat(postgresSQLJobSqlProvider.getInsertSql(), is(INSERT_JOB_SQL));
    }

    @Test
    public void shouldReturnPostgresUpdateJobDataSQL() {

        final PostgresJobSqlProvider postgresSQLJobSqlProvider = new PostgresJobSqlProvider();
        assertThat(postgresSQLJobSqlProvider.getUpdateJobDataSql(), is(UPDATE_JOB_DATA_SQL));
    }
}
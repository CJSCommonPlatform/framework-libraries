package uk.gov.moj.cpp.jobmanager.it;

import static java.time.ZonedDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static java.util.stream.IntStream.range;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import uk.gov.justice.services.cdi.InitialContextProducer;
import uk.gov.justice.services.cdi.LoggerProducer;
import uk.gov.justice.services.common.configuration.GlobalValueProducer;
import uk.gov.justice.services.common.configuration.JndiBasedServiceContextNameProvider;
import uk.gov.justice.services.common.configuration.ValueProducer;
import uk.gov.justice.services.jdbc.persistence.JdbcResultSetStreamer;
import uk.gov.justice.services.jdbc.persistence.PreparedStatementWrapperFactory;
import uk.gov.justice.services.test.utils.core.messaging.Poller;
import uk.gov.moj.cpp.jobmanager.it.util.OpenEjbConfigurationBuilder;
import uk.gov.moj.cpp.jobmanager.it.util.OpenEjbJobJdbcRepository;
import uk.gov.moj.cpp.jobstore.persistence.JdbcJobStoreDataSourceProvider;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.persistence.JobRepository;
import uk.gov.moj.cpp.jobstore.persistence.JobSqlProvider;
import uk.gov.moj.cpp.jobstore.persistence.PostgresJobSqlProvider;
import uk.gov.moj.cpp.jobstore.service.JobService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.Application;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Configuration;
import org.apache.openejb.testing.Module;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ApplicationComposer.class)
public class JobServiceIT {

    private static final String LIQUIBASE_JOB_STORE_DB_CHANGELOG_XML = "liquibase/jobstore-db-changelog.xml";

    // given a list of jobs for workers
    final Map<UUID, List<Job>> workersToJobs = new HashMap();

    @Inject
    OpenEjbJobJdbcRepository testJobJdbcRepository;

    @Module
    @Classes(cdi = true, value = {
            JobService.class,
            JobRepository.class,
            JdbcJobStoreDataSourceProvider.class,
            PreparedStatementWrapperFactory.class,
            JdbcResultSetStreamer.class,
            JobSqlProvider.class,
            LoggerProducer.class,
            OpenEjbJobJdbcRepository.class,
            GlobalValueProducer.class,
            ValueProducer.class,
            JndiBasedServiceContextNameProvider.class,
            InitialContextProducer.class,
            Integer.class
    }, cdiAlternatives = {PostgresJobSqlProvider.class})

    public WebApp war() {
        return new WebApp()
                .contextRoot("framework-test")
                .addServlet("ServiceApp", Application.class.getName());
    }


    @Resource(name = "openejb/Resource/jobStore")
    private DataSource dataSource;

    @Inject
    JobService jobService;

    @Before
    public void setup() throws Exception {
        final InitialContext initialContext = new InitialContext();
        initialContext.bind("java:/app/JobServiceIT/DS.jobstore", dataSource);
        initEventDatabase();
        setField(jobService, "jobCount", "10");
    }

    @Configuration
    public Properties configuration() {
        return OpenEjbConfigurationBuilder.createOpenEjbConfigurationBuilder()
                .addInitialContext()
                .addHttpEjbPort(8080)
                .addPostgresqlJobStore()
                .build();
    }

    @Inject
    UserTransaction userTransaction;

    public void initEventDatabase() throws Exception {

        final Liquibase eventStoreLiquibase = new Liquibase(LIQUIBASE_JOB_STORE_DB_CHANGELOG_XML,
                new ClassLoaderResourceAccessor(), new JdbcConnection(dataSource.getConnection()));

        eventStoreLiquibase.update("");

        setField(jobService, "jobRepository", testJobJdbcRepository);
    }

    @Test
    public void shouldUpdateJobsThatHaveAWorkerIdAndBeenIdleForMoreThanOneHour() throws Exception {
        userTransaction.begin();
        testJobJdbcRepository.cleanJobTables();
        testJobJdbcRepository.createJobs(20);
        testJobJdbcRepository.createIdleJobs(10, of(now().minus(65, MINUTES)), now());
        testJobJdbcRepository.createIdleJobs(10, of(now().minus(30, MINUTES)), now());
        userTransaction.commit();

        userTransaction.begin();
        for (int i = 0; i < 4; i++) {
            final UUID workerId = randomUUID();
            jobService.getUnassignedJobsFor(workerId);
            collectInfo(workerId);
        }
        userTransaction.commit();

        detectDuplicates();
        assertThat(testJobJdbcRepository.jobsProcessed(), CoreMatchers.is(30));
    }

    @Test
    public void shouldUpdateJobsThatHaveAWorkerIdAndBeenIdleForMoreThanOneHourAndReadyToStart() throws Exception {
        userTransaction.begin();
        testJobJdbcRepository.cleanJobTables();
        testJobJdbcRepository.createJobs(20);
        testJobJdbcRepository.createIdleJobs(5, of(now().minus(65, MINUTES)), now().plus(30, MINUTES));
        testJobJdbcRepository.createIdleJobs(5, of(now().minus(65, MINUTES)), now());
        testJobJdbcRepository.createIdleJobs(10, of(now().minus(30, MINUTES)), now());
        userTransaction.commit();

        userTransaction.begin();
        for (int i = 0; i < 4; i++) {
            final UUID workerId = randomUUID();
            jobService.getUnassignedJobsFor(workerId);
            collectInfo(workerId);
        }
        userTransaction.commit();

        detectDuplicates();
        assertThat(testJobJdbcRepository.jobsProcessed(), CoreMatchers.is(25));
    }

    @Test
    public void shouldNotPerformDuplicateJobUpdates() throws Exception {
        userTransaction.begin();
        testJobJdbcRepository.cleanJobTables();
        testJobJdbcRepository.createJobs(1000);
        userTransaction.commit();

        final CyclicBarrier gate = new CyclicBarrier(21);

        range(0, 20)
                .mapToObj(threadNo -> getThreadCurrentImpl(gate))
                .forEach(Thread::start);

        gate.await();

        detectDuplicates();
        assertThat(testJobJdbcRepository.jobsProcessed(), CoreMatchers.is(200));
    }


    private Thread getThreadCurrentImpl(final CyclicBarrier gate) {
        return new Thread(() -> {
            try {
                gate.await();
                userTransaction.begin();
                final UUID workerId = randomUUID();
                jobService.getUnassignedJobsFor(workerId);
                collectInfo(workerId);
                userTransaction.commit();

            } catch (InterruptedException | NotSupportedException | SystemException | BrokenBarrierException |
                    RollbackException | HeuristicMixedException | HeuristicRollbackException | SQLException e) {
                throw new RuntimeException("Failed to complete transaction");
            }
        });
    }

    private synchronized void collectInfo(final UUID workerId) throws SQLException {
        final Stream<Job> jobsForWorker = testJobJdbcRepository.getProcessedRecords(workerId);

        final StringBuilder sb = new StringBuilder("collectInfo for WorkerId: ").append(workerId);
        sb.append(", COUNT: %s").append("\n");

        final List<Job> jobs = new ArrayList<>();
        jobsForWorker.forEach(job -> {
            jobs.add(job);
            sb.append("\t").append(job.getJobId()).append("\n");
        });
        workersToJobs.put(workerId, jobs);
    }


    private void detectDuplicates() throws InterruptedException {
        final Set<UUID> jobs = new HashSet<>();
        final List<UUID> duplicates = new ArrayList<>();

        final Poller poller = new Poller(10, 100L);
        poller.pollUntilFound(() -> workersToJobs.size() == 3 ? Optional.of("Success") : Optional.empty());

        testJobJdbcRepository.collectDuplicates(jobs, duplicates, workersToJobs);

        duplicates.forEach(job -> System.out.println(" Duplicated Job: " + job));
        assertThat(duplicates.size(), is(0));
    }
}

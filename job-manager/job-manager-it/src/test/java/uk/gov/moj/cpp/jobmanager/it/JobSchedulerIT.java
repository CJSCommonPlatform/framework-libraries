package uk.gov.moj.cpp.jobmanager.it;

import static java.lang.Thread.sleep;
import static java.util.stream.IntStream.range;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import uk.gov.justice.framework.libraries.datasource.providers.jobstore.JndiJobStoreDataSourceProvider;
import uk.gov.justice.services.cdi.InitialContextProducer;
import uk.gov.justice.services.cdi.LoggerProducer;
import uk.gov.justice.services.common.configuration.GlobalValueProducer;
import uk.gov.justice.services.common.configuration.JndiBasedServiceContextNameProvider;
import uk.gov.justice.services.common.configuration.ValueProducer;
import uk.gov.justice.services.common.converter.JsonObjectToObjectConverter;
import uk.gov.justice.services.common.converter.ObjectToJsonObjectConverter;
import uk.gov.justice.services.common.converter.StringToJsonObjectConverter;
import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.jdbc.persistence.InitialContextFactory;
import uk.gov.moj.cpp.jobmanager.it.util.OpenEjbConfigurationBuilder;
import uk.gov.moj.cpp.jobmanager.it.util.OpenEjbJobJdbcRepository;
import uk.gov.moj.cpp.jobstore.api.ExecutionService;
import uk.gov.moj.cpp.jobstore.persistence.JdbcResultSetStreamer;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.persistence.JobRepository;
import uk.gov.moj.cpp.jobstore.persistence.JobStoreConfiguration;
import uk.gov.moj.cpp.jobstore.persistence.PreparedStatementWrapperFactory;
import uk.gov.moj.cpp.jobstore.service.JobService;
import uk.gov.moj.cpp.task.execution.JobScheduler;
import uk.gov.moj.cpp.task.execution.JobStoreSchedulerPrioritySelector;
import uk.gov.moj.cpp.task.execution.RandomPercentageProvider;
import uk.gov.moj.cpp.task.extension.SampleTask;
import uk.gov.moj.cpp.task.extension.TaskRegistry;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.junit5.RunWithApplicationComposer;
import org.apache.openejb.testing.Application;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Configuration;
import org.apache.openejb.testing.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@RunWithApplicationComposer
public class JobSchedulerIT {

    private static final String LIQUIBASE_JOB_STORE_DB_CHANGELOG_XML = "liquibase/jobstore-db-changelog.xml";

    @Inject
    OpenEjbJobJdbcRepository testJobJdbcRepository;

    @Module
    @Classes(cdi = true, value = {
            JobService.class,
            ExecutionService.class,
            JobRepository.class,
            JndiJobStoreDataSourceProvider.class,
            PreparedStatementWrapperFactory.class,
            JdbcResultSetStreamer.class,
            OpenEjbJobJdbcRepository.class,
            JobScheduler.class,
            GlobalValueProducer.class,
            TaskRegistry.class,

            StringToJsonObjectConverter.class,
            ObjectMapper.class,

            JndiBasedServiceContextNameProvider.class,
            ValueProducer.class,
            GlobalValueProducer.class,

            JsonObjectToObjectConverter.class,
            ObjectToJsonObjectConverter.class,
            UtcClock.class,
            InitialContextProducer.class,
            LoggerProducer.class,
            SampleTask.class,

            InitialContextFactory.class,

            JobStoreConfiguration.class,
            JobStoreSchedulerPrioritySelector.class,
            DummyJobStoreSchedulerPrioritySelector.class,
            RandomPercentageProvider.class
    })

    public WebApp war() {
        return new WebApp()
                .contextRoot("framework-test")
                .addServlet("ServiceApp", Application.class.getName());
    }

    @Resource(name = "openejb/Resource/jobStore")
    private DataSource dataSource;

    @Inject
    private JobService jobService;

    @Inject
    private JobScheduler jobScheduler;

    @BeforeEach
    public void setup() throws Exception {
        final InitialContext initialContext = new InitialContext();
        initialContext.bind("java:/app/JobSchedulerIT/DS.jobstore", dataSource);
        initEventDatabase();
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
        Liquibase eventStoreLiquibase = new Liquibase(LIQUIBASE_JOB_STORE_DB_CHANGELOG_XML,
                new ClassLoaderResourceAccessor(), new JdbcConnection(dataSource.getConnection()));

        eventStoreLiquibase.update("");

        setField(jobService, "jobRepository", testJobJdbcRepository);
    }

    @Test
    public void shouldNotPerformDuplicateJobUpdates() throws Exception {
        userTransaction.begin();
        testJobJdbcRepository.cleanJobTables();
        testJobJdbcRepository.createJobs(50);
        userTransaction.commit();

        userTransaction.begin();

        final CyclicBarrier gate = new CyclicBarrier(4);

        range(0, 3)
                .mapToObj(threadNo -> getThreadCurrentImpl(gate))
                .forEach(Thread::start);

        gate.await();

        sleep(3000);
        userTransaction.commit();

        assertThat(testJobJdbcRepository.jobsNotProcessed(), is(20));
    }


    private Thread getThreadCurrentImpl(final CyclicBarrier gate) {
        return new Thread(() -> {
            try {
                gate.await();
                userTransaction.begin();

                jobScheduler.fetchUnassignedJobs();
                assertExpectedJobAssignments();

                userTransaction.commit();

            } catch (InterruptedException | NotSupportedException | SystemException | BrokenBarrierException |
                    RollbackException | HeuristicMixedException | HeuristicRollbackException | SQLException e) {
                throw new RuntimeException("Failed to complete transaction ");
            }

        });
    }

    private void assertExpectedJobAssignments() throws SQLException {
        final Stream<Job> jobStream = testJobJdbcRepository.getProcessedRecords();
        final Map<UUID, List<Job>> jobByWorkerIdMap = jobStream.collect(Collectors.groupingBy(x -> x.getWorkerId().get()));
        final Set<UUID> jobs = new HashSet<>();
        final List<UUID> duplicates = new ArrayList<>();

        testJobJdbcRepository.collectDuplicates(jobs, duplicates, jobByWorkerIdMap);

        duplicates.forEach(job -> System.out.println(" Duplicated Job: " + job));
        assertThat(duplicates.size(), is(0));
    }
}

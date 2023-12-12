package uk.gov.moj.cpp.jobmanager.example;

import static uk.gov.moj.cpp.jobmanager.it.util.OpenEjbConfigurationBuilder.createOpenEjbConfigurationBuilder;

import uk.gov.justice.services.cdi.InitialContextProducer;
import uk.gov.justice.services.cdi.LoggerProducer;
import uk.gov.justice.services.common.configuration.JndiBasedServiceContextNameProvider;
import uk.gov.justice.services.common.converter.JsonObjectConvertersProducer;
import uk.gov.justice.services.common.converter.JsonObjectToObjectConverter;
import uk.gov.justice.services.common.converter.ObjectToJsonObjectConverter;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;
import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.moj.cpp.jobmanager.example.task.BakeCakeTask;
import uk.gov.moj.cpp.jobmanager.example.task.FillCakeTinTask;
import uk.gov.moj.cpp.jobmanager.example.task.GetIngredientsTask;
import uk.gov.moj.cpp.jobmanager.example.task.GetUtensilsTask;
import uk.gov.moj.cpp.jobmanager.example.task.JobUtil;
import uk.gov.moj.cpp.jobmanager.example.task.MixIngredientsTask;
import uk.gov.moj.cpp.jobmanager.example.task.SliceAndEatCakeTask;
import uk.gov.moj.cpp.jobmanager.example.task.SwitchOvenOnTask;
import uk.gov.moj.cpp.jobmanager.example.util.PropertiesFileValueProducer;
import uk.gov.moj.cpp.jobmanager.it.util.OpenEjbJobJdbcRepository;
import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;
import uk.gov.moj.cpp.jobstore.persistence.JdbcJobStoreDataSourceProvider;
import uk.gov.moj.cpp.jobstore.persistence.JdbcResultSetStreamer;
import uk.gov.moj.cpp.jobstore.persistence.JobRepository;
import uk.gov.moj.cpp.jobstore.persistence.JobSqlProvider;
import uk.gov.moj.cpp.jobstore.persistence.PostgresJobSqlProvider;
import uk.gov.moj.cpp.jobstore.persistence.PreparedStatementWrapperFactory;
import uk.gov.moj.cpp.jobstore.service.JobService;
import uk.gov.moj.cpp.task.execution.DefaultExecutionService;
import uk.gov.moj.cpp.task.execution.JobScheduler;
import uk.gov.moj.cpp.task.extension.TaskRegistry;

import java.util.Properties;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.commons.logging.LogFactory;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.jee.jpa.unit.Persistence;
import org.apache.openejb.jee.jpa.unit.PersistenceUnit;
import org.apache.openejb.junit5.RunWithApplicationComposer;
import org.apache.openejb.testing.Application;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Configuration;
import org.apache.openejb.testing.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@RunWithApplicationComposer
public class BakeryServiceIT {

    @Inject
    private uk.gov.moj.cpp.jobmanager.it.util.OpenEjbJobJdbcRepository repository;

    @Inject
    private BakeryService bakeryService;

    @Resource(name = "openejb/Resource/jobStore")
    private DataSource dataSource;

    @Module
    @Classes(cdi = true, value = {
            JobService.class,
            DefaultExecutionService.class,
            JobRepository.class,
            TaskRegistry.class,
            JobScheduler.class,
            SwitchOvenOnTask.class,
            JobUtil.class,
            ExecutableTask.class,
            GetIngredientsTask.class,
            GetUtensilsTask.class,
            MixIngredientsTask.class,
            FillCakeTinTask.class,
            BakeCakeTask.class,
            SliceAndEatCakeTask.class,
            JdbcJobStoreDataSourceProvider.class,
            PreparedStatementWrapperFactory.class,
            JdbcResultSetStreamer.class,
            JobSqlProvider.class,
            LoggerProducer.class,
            OpenEjbJobJdbcRepository.class,
            JndiBasedServiceContextNameProvider.class,
            PropertiesFileValueProducer.class,
            BakeryService.class,
            ObjectToJsonObjectConverter.class,
            JsonObjectToObjectConverter.class,
            LogFactory.class,
            Persistence.class,
            PersistenceUnit.class,
            ObjectMapperProducer.class,
            InitialContextProducer.class,
            UtcClock.class,

            JsonObjectConvertersProducer.class
    },
            cdiAlternatives = {PostgresJobSqlProvider.class}
    )

    public WebApp war() {
        return new WebApp()
                .contextRoot("bakeryservice-test")
                .addServlet("ServiceApp", Application.class.getName());
    }

    @Configuration
    public Properties configuration() {
        return createOpenEjbConfigurationBuilder()
                .addInitialContext()
                .addHttpEjbPort(8080)
                .addPostgresViewStore()
                .build();
    }

    @BeforeEach
    public void setup() throws Exception {
        final InitialContext initialContext = new InitialContext();
        initialContext.bind("java:/app/BakeryServiceIT/DS.jobstore", dataSource);
        initJobStoreDatabase();
    }

    public void initJobStoreDatabase() throws Exception {
        new Liquibase(
                "liquibase/jobstore-db-changelog.xml",
                new ClassLoaderResourceAccessor(),
                new JdbcConnection(dataSource.getConnection()))
                .update("");
    }

    @Test
    public void shouldMakeACake() {

        bakeryService.makeCake();
        repository.waitForAllJobsToBeProcessed();
    }
}

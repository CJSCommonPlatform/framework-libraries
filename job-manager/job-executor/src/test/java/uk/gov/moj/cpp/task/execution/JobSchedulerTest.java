package uk.gov.moj.cpp.task.execution;

import static java.util.stream.Stream.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.LOW;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.MEDIUM;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.persistence.JobStoreConfiguration;
import uk.gov.moj.cpp.jobstore.persistence.Priority;
import uk.gov.moj.cpp.jobstore.service.JobService;
import uk.gov.moj.cpp.task.extension.TaskRegistry;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class JobSchedulerTest {

    @Mock
    private ManagedExecutorService executorService;

    @Mock
    private JobService jobService;

    @Mock
    private Job job;

    @Mock
    private TimerService timerService;

    @Mock
    private Logger logger;

    @Mock
    private TaskRegistry taskRegistry;

    @Mock
    private UserTransaction userTransaction;

    @Mock
    private JobStoreConfiguration jobStoreConfiguration;

    @Mock
    private JobStoreSchedulerPrioritySelector jobStoreSchedulerPrioritySelector;

    @Mock
    private UtcClock clock;

    @Captor
    private ArgumentCaptor<TimerConfig> timerConfigArgumentCaptor;

    @InjectMocks
    private JobScheduler jobExecutor;

    @Test
    public void shouldExecuteFetchedJobs() {

        final List<Priority> priorities = List.of(HIGH, MEDIUM, LOW);
        when(logger.isDebugEnabled()).thenReturn(true);
        when(jobStoreSchedulerPrioritySelector.selectOrderedPriorities()).thenReturn(priorities);
        when(jobService.getUnassignedJobsFor(any(UUID.class), eq(priorities))).thenReturn(Stream.of(job));
        when(job.getPriority()).thenReturn(MEDIUM);

        jobExecutor.fetchUnassignedJobs();

        final InOrder inOrder = inOrder(executorService, logger);

        inOrder.verify(logger).debug("Fetching new jobs from jobstore in priority order [HIGH, MEDIUM, LOW]");
        inOrder.verify(logger).debug("Found 1 MEDIUM priority job(s) to run from jobstore");
        inOrder.verify(executorService).submit(any(JobExecutor.class));
    }

    @Test
    public void shouldLogIfNoNewJobsFound() throws Exception {

        final List<Priority> priorities = List.of(HIGH, MEDIUM, LOW);
        when(logger.isDebugEnabled()).thenReturn(true);
        when(jobStoreSchedulerPrioritySelector.selectOrderedPriorities()).thenReturn(priorities);
        when(jobService.getUnassignedJobsFor(any(UUID.class), eq(priorities))).thenReturn(empty());

        jobExecutor.fetchUnassignedJobs();

        final InOrder inOrder = inOrder(logger);

        inOrder.verify(logger).debug("Fetching new jobs from jobstore in priority order [HIGH, MEDIUM, LOW]");
        inOrder.verify(logger).debug("No new jobs found in jobstore");

        verifyNoInteractions(executorService);
    }

    @Test
    public void shouldNotAttemptToExecuteEmptyStreamOfJobs() {

        final List<Priority> priorities = List.of(HIGH, MEDIUM, LOW);
        when(jobStoreSchedulerPrioritySelector.selectOrderedPriorities()).thenReturn(priorities);
        when(jobService.getUnassignedJobsFor(any(UUID.class), eq(priorities))).thenReturn(Stream.of());

        jobExecutor.fetchUnassignedJobs();

        verifyNoInteractions(executorService);
    }

    @Test
    public void shouldSetTimerTaskOnPostConstruct() {

        final long timerIntervalSeconds = 1000;
        final long timerStartWaitSeconds = 100;
        final String moduleName = "TEST_TIMER";

        when(jobStoreConfiguration.getTimerIntervalMilliseconds()).thenReturn(timerIntervalSeconds);
        when(jobStoreConfiguration.getTimerStartWaitMilliseconds()).thenReturn(timerStartWaitSeconds);
        when(jobStoreConfiguration.getModuleName()).thenReturn(moduleName);

        jobExecutor.init();

        verify(timerService).createIntervalTimer(eq(timerStartWaitSeconds), eq(timerIntervalSeconds), timerConfigArgumentCaptor.capture());

        assertFalse(timerConfigArgumentCaptor.getValue().isPersistent());
        assertThat(timerConfigArgumentCaptor.getValue().getInfo(), is("TEST_TIMER.job-manager.job.timer"));
    }


    @Test
    public void shouldRollbackTransactionWhenAnExceptionOccurs() throws SystemException, NotSupportedException {

        doThrow(new NotSupportedException()).when(userTransaction).begin();

        jobExecutor.fetchUnassignedJobs();

        verify(userTransaction).rollback();
    }

}
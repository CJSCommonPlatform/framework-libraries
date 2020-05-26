package uk.gov.moj.cpp.task.execution;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.service.JobService;
import uk.gov.moj.cpp.task.extension.TaskRegistry;

import java.util.UUID;
import java.util.stream.Stream;

import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
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
    private UtcClock clock;

    @Captor
    private ArgumentCaptor<TimerConfig> timerConfigArgumentCaptor;

    @InjectMocks
    private JobScheduler jobExecutor;

    @Test
    public void shouldExecuteFetchedJobs() {
        when(jobService.getUnassignedJobsFor(any(UUID.class))).thenReturn(Stream.of(job));

        jobExecutor.fetchUnassignedJobs();

        verify(executorService).submit(any(JobExecutor.class));
    }

    @Test
    public void shouldNotAttemptToExecuteEmptyStreamOfJobs() {
        when(jobService.getUnassignedJobsFor(any(UUID.class))).thenReturn(Stream.of());

        jobExecutor.fetchUnassignedJobs();

        verifyZeroInteractions(executorService);
    }

    @Test
    public void shouldSetTimerTaskOnPostConstruct() {
        jobExecutor.timerIntervalSeconds = "1000";
        jobExecutor.timerStartWaitSeconds = "100";
        jobExecutor.moduleName = "TEST_TIMER";
        jobExecutor.init();

        verify(timerService).createIntervalTimer(eq(100L), eq(1000L), timerConfigArgumentCaptor.capture());

        assertFalse(timerConfigArgumentCaptor.getValue().isPersistent());
        assertThat(timerConfigArgumentCaptor.getValue().getInfo(), is("TEST_TIMER.job-manager.job.timer"));
    }


    @Test
    public void shouldRollbackTransactionWhenAnExceptionOccurs() throws SystemException, NotSupportedException {
        when(jobService.getUnassignedJobsFor(any(UUID.class))).thenReturn(Stream.of(job));
        doThrow(new NotSupportedException()).when(userTransaction).begin();

        jobExecutor.fetchUnassignedJobs();

        verify(userTransaction).rollback();
    }

}
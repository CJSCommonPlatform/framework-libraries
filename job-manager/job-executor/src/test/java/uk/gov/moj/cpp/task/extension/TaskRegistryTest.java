package uk.gov.moj.cpp.task.extension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;

import java.util.Iterator;
import java.util.Optional;

import javax.enterprise.inject.Instance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class TaskRegistryTest {

    @Mock
    private Instance<ExecutableTask> taskBeanProxyMock;

    @Mock
    private TaskFoundEvent taskFoundEventMock;

    @Mock
    private Logger logger;


    @InjectMocks
    private TaskRegistry taskRegistry;

    private SampleTaskCdiProxy sampleTaskCdiProxy = new SampleTaskCdiProxy();

    @Test
    public void shouldAddTaskToRegistry() {
        when(taskFoundEventMock.getClazz()).thenReturn((Class) SampleTask.class);

        when(taskBeanProxyMock.iterator()).thenReturn(new TestIterator());

        taskRegistry.register(taskFoundEventMock);

        final Optional<ExecutableTask> task = taskRegistry.getTask("sample-task");
        assertTrue(task.isPresent());
        assertThat(taskRegistry.getTask("sample-task").get(), instanceOf(SampleTaskCdiProxy.class));

    }


    class TestIterator implements Iterator<ExecutableTask> {
        int count = 1;
        @Override
        public boolean hasNext() {
            return count > 0;
        }

        @Override
        public ExecutableTask next() {
            count = 0;
            return sampleTaskCdiProxy;
        }
    };
}

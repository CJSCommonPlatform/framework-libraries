package uk.gov.moj.cpp.task.extension;


import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import uk.gov.moj.cpp.jobstore.api.annotation.Task;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TaskScannerTest {

    @Mock
    private ProcessAnnotatedType processAnnotatedType;

    @Mock
    private AnnotatedType annotatedType;

    @Mock
    private BeanManager beanManager;

    @InjectMocks
    private TaskScanner taskScanner;

    @Test
    public void shouldNotifyTaskFoundEvent() {
        mockProcessAnnotatedType(Task.class);
        taskScanner.processAnnotatedType(processAnnotatedType);
        taskScanner.afterDeploymentValidation(null, beanManager);

        final ArgumentCaptor<TaskFoundEvent> captor = ArgumentCaptor.forClass(TaskFoundEvent.class);
        verify(beanManager).fireEvent(captor.capture());
        assertThat(captor.getValue(), instanceOf(TaskFoundEvent.class));
    }

    @Test
    public void shouldNotFireNotifyTaskFoundEventForNonNotifyTaskBeans() {
        mockProcessAnnotatedType(String.class);
        taskScanner.processAnnotatedType(processAnnotatedType);
        taskScanner.afterDeploymentValidation(null, beanManager);

        verify(beanManager, never()).fireEvent(any());
    }

    private void mockProcessAnnotatedType(final Class clazz) {
        doReturn(annotatedType).when(processAnnotatedType).getAnnotatedType();
        doReturn(true).when(annotatedType).isAnnotationPresent(clazz);
    }

}
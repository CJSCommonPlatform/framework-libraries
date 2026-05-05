package uk.gov.moj.cpp.task.extension;


import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.moj.cpp.jobstore.api.annotation.Task;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TaskScannerTest {

    @Mock
    private ProcessAnnotatedType processAnnotatedType;

    @Mock
    private AnnotatedType annotatedType;

    @Mock
    private BeanManager beanManager;

    @Mock
    private Event<Object> event;

    @InjectMocks
    private TaskScanner taskScanner;

    @Test
    public void shouldNotifyTaskFoundEvent() {
        doReturn(annotatedType).when(processAnnotatedType).getAnnotatedType();
        doReturn(true).when(annotatedType).isAnnotationPresent(Task.class);
        when(beanManager.getEvent()).thenReturn(event);
        taskScanner.processAnnotatedType(processAnnotatedType);
        taskScanner.afterDeploymentValidation(null, beanManager);

        final ArgumentCaptor<TaskFoundEvent> captor = ArgumentCaptor.forClass(TaskFoundEvent.class);
        verify(event).fire(captor.capture());
        assertThat(captor.getValue(), instanceOf(TaskFoundEvent.class));
    }

    @Test
    public void shouldNotFireNotifyTaskFoundEventForNonNotifyTaskBeans() {
        doReturn(annotatedType).when(processAnnotatedType).getAnnotatedType();
        doReturn(false).when(annotatedType).isAnnotationPresent(Task.class);

        taskScanner.processAnnotatedType(processAnnotatedType);
        taskScanner.afterDeploymentValidation(null, beanManager);

        verify(event, never()).fire(any());
    }
}
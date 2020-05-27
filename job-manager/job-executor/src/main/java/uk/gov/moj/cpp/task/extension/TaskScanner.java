package uk.gov.moj.cpp.task.extension;

import static java.util.Collections.synchronizedList;

import uk.gov.moj.cpp.jobstore.api.annotation.Task;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class TaskScanner implements Extension {

    private final List<Object> events = synchronizedList(new ArrayList<>());

    @SuppressWarnings("unused")
    <T> void processAnnotatedType(@Observes final ProcessAnnotatedType<T> pat) {
        final AnnotatedType<T> annotatedType = pat.getAnnotatedType();

        if (annotatedType.isAnnotationPresent(Task.class)) {
            events.add(new TaskFoundEvent(annotatedType.getJavaClass()));
        }
    }

    @SuppressWarnings("unused")
    void afterDeploymentValidation(@Observes final AfterDeploymentValidation event, final BeanManager beanManager) {
        events.forEach(beanManager::fireEvent);
    }
}

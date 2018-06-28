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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskScanner implements Extension {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskScanner.class);

    private List<Object> events = synchronizedList(new ArrayList<>());

    @SuppressWarnings("unused")
    <T> void processAnnotatedType(@Observes final ProcessAnnotatedType<T> pat) {
        final AnnotatedType<T> annotatedType = pat.getAnnotatedType();

        if (annotatedType.isAnnotationPresent(Task.class)) {
            LOGGER.info("Found 'TaskExecutor' class");
            events.add(new TaskFoundEvent(annotatedType.getJavaClass()));
        }
    }

    @SuppressWarnings("unused")
    void afterDeploymentValidation(@Observes final AfterDeploymentValidation event, final BeanManager beanManager) {
        events.forEach(beanManager::fireEvent);
    }
}

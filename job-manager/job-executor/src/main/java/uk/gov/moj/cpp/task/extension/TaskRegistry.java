package uk.gov.moj.cpp.task.extension;

import uk.gov.moj.cpp.jobstore.api.annotation.Task;
import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;

@ApplicationScoped
public class TaskRegistry {

    @Inject
    private  Logger logger;

    private final Map<String, ExecutableTask> taskProxyByNameMap = new HashMap<>();

    @Inject
    private Instance<ExecutableTask> taskBeanProxy;

    public void register(@Observes final TaskFoundEvent event) {

        final Class taskClass = event.getClazz();
        final String taskName = ((Task) taskClass.getAnnotation(Task.class)).value();

        logger.info("Notified of Work Task [type={}], [name={}]", taskClass, taskName);

        for (final ExecutableTask taskProxy : taskBeanProxy) {
            final String proxyClassName = taskProxy.getClass().getName();
            if (proxyClassName.startsWith(taskClass.getName())) {
                taskProxyByNameMap.putIfAbsent(taskName, taskProxy);
                logger.info("Registering Work Task proxy [type={}], [name={}]", taskProxy, taskName);
                break;
            }
        }

        if (taskProxyByNameMap.get(taskName) == null) {
            logger.error("No Injected proxy class provided for task [{}]", taskClass);
        }

    }

    public Optional<ExecutableTask> getTask(final String taskName) {
        return Optional.ofNullable(taskProxyByNameMap.get(taskName));
    }

    public Integer findRetryAttemptsRemainingFor(final String taskName) {
        return getTask(taskName)
                .map(this::findRetryAttemptsRemainingFor)
                .orElse(0);
    }

    private Integer findRetryAttemptsRemainingFor(final ExecutableTask task) {
        return task.getRetryDurationsInSecs()
                .map(List::size)
                .orElse(0);
    }
}

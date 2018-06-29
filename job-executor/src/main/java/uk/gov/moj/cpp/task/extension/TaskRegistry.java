package uk.gov.moj.cpp.task.extension;

import uk.gov.moj.cpp.jobstore.api.annotation.Task;
import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class TaskRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRegistry.class);

    private final Map<String, ExecutableTask> taskProxyByNameMap = new HashMap<>();

    @Inject
    private Instance<ExecutableTask> taskBeanProxy;



    public void register(@Observes final TaskFoundEvent event) {

        final Class taskClass = event.getClazz();
        final String taskName = ((Task) taskClass.getAnnotation(Task.class)).value();

        LOGGER.info("Notified of Work Task [type={}], [name={}]", taskClass, taskName);

        for (ExecutableTask taskProxy : taskBeanProxy) {
            final String proxyClassName = taskProxy.getClass().getName();
            if (proxyClassName.startsWith(taskClass.getName())) {
                taskProxyByNameMap.putIfAbsent(taskName, taskProxy);
                LOGGER.info("Registering Work Task proxy [type={}], [name={}]", taskProxy, taskName);
                break;
            }
        }

        if (taskProxyByNameMap.get(taskName) == null) {
            LOGGER.error("No Injected proxy class provided for task [{}]", taskClass);
        }

    }

    public Optional<ExecutableTask> getTask(final String taskName) {
        return Optional.ofNullable(taskProxyByNameMap.get(taskName));
    }
}

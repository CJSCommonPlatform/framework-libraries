package uk.gov.moj.cpp.task.extension;

public class TaskFoundEvent {

    private final Class<?> clazz;

    public TaskFoundEvent(final Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
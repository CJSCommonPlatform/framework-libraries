package uk.gov.moj.cpp.jobstore.api.task;

import uk.gov.moj.cpp.jobstore.persistence.Job;

public interface ExecutableTask {

    Job execute(final Job job);

}

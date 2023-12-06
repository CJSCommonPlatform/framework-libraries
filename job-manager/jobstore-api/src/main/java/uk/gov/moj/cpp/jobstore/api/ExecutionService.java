package uk.gov.moj.cpp.jobstore.api;


import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;

public interface ExecutionService {

    void executeWith(final ExecutionInfo executionInfo);
}

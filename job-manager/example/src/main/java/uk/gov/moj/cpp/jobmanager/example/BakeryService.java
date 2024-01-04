package uk.gov.moj.cpp.jobmanager.example;

import static java.time.ZonedDateTime.now;

import uk.gov.justice.services.common.converter.ObjectToJsonObjectConverter;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionStatus;
import uk.gov.moj.cpp.task.execution.DefaultExecutionService;

import javax.inject.Inject;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BakeryService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    ObjectToJsonObjectConverter objectConverter;

    @Inject
    DefaultExecutionService executionService;

    @Inject
    UserTransaction userTransaction;

    public void makeCake() {

        final MakeCakeWorkflow firstTask = MakeCakeWorkflow.firstTask();

        final ExecutionInfo startCakeExecutionInfo = new ExecutionInfo(objectConverter.convert(firstTask.getTaskData()), firstTask.toString(), now(), ExecutionStatus.STARTED, true, 1);

        try {
            userTransaction.begin();
            executionService.executeWith(startCakeExecutionInfo);
            userTransaction.commit();

        } catch (Exception ex) {
            logger.error("Unexpected exception during transaction, attempting rollback...", ex);

            try {
                if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION) {
                    userTransaction.rollback();
                    logger.info("Transaction rolled back successfully");
                }

            } catch(SystemException e1){
                logger.error("Unexpected exception during transaction rollback, rollback maybe incomplete", e1);
            }
        }
    }
}

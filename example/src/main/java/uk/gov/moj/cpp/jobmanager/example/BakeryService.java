package uk.gov.moj.cpp.jobmanager.example;

import static java.time.ZonedDateTime.now;
import static java.util.UUID.randomUUID;

import uk.gov.justice.services.common.converter.ObjectToJsonObjectConverter;
import uk.gov.moj.cpp.jobstore.api.JobRequest;
import uk.gov.moj.cpp.jobstore.api.JobService;

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
    JobService jobService;

    @Inject
    UserTransaction userTransaction;

    public void makeCake() {

        final MakeCakeWorkflow firstTask = MakeCakeWorkflow.firstTask();

        final JobRequest startCakeJobRequest = new JobRequest(randomUUID(), objectConverter.convert(firstTask.getTaskData()), firstTask.toString(), now());

        try {
            userTransaction.begin();
            jobService.createJob(startCakeJobRequest);
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

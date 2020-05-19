package uk.gov.moj.cpp.jobstore.persistence;


import uk.gov.justice.services.common.configuration.GlobalValue;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.slf4j.Logger;


public class JobSqlProviderProducer {
    private static final String INSTANTIATION_ERROR_MSG = "Could not instantiate Job SQL provider strategy.";

    @GlobalValue(key = "job.manager.persistence.insertion.strategy", defaultValue = "uk.gov.moj.cpp.jobstore.persistence.PostgresJobSqlProvider")
    @Inject
    String strategyClass;

    @Inject
    Logger logger;

    @Produces
    public JobSqlProvider jobSqlProvider() {
        logger.info("Instantiating {}", strategyClass);
        try {
            final Class<?> clazz = Class.forName(strategyClass);
            return (JobSqlProvider) clazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new IllegalArgumentException(INSTANTIATION_ERROR_MSG, e);
        }
    }
}

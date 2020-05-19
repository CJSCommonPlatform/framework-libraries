package uk.gov.justice.services.fileservice.repository;

import uk.gov.justice.services.common.configuration.GlobalValue;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.slf4j.Logger;

public class MetadataSqlProviderProducer {

    private static final String INSTANTIATION_ERROR_MSG = "Could not instantiate File Service SQL provider strategy.";

    @Inject
    @GlobalValue(key = "fileservice.metadata.sql.provider.strategy", defaultValue = "uk.gov.justice.services.fileservice.repository.PostgresMetadataSqlProvider")
    String strategyClass;

    @Inject
    Logger logger;

    @Produces
    public MetadataSqlProvider metadataSqlProvider() {
        logger.info("Instantiating {}", strategyClass);
        try {
            final Class<?> clazz = Class.forName(strategyClass);
            return (MetadataSqlProvider) clazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new IllegalArgumentException(INSTANTIATION_ERROR_MSG, e);
        }
    }
}
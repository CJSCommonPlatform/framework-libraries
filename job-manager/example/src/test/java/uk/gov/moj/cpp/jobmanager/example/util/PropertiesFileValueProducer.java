package uk.gov.moj.cpp.jobmanager.example.util;

import uk.gov.justice.services.common.configuration.Value;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
    This Producer loads its properties from a properties file. This
    class simplifies the setting of @Value annotations in the source code being tested,
    specifically when using ApplicationComposer, OpenEjb etc where setting JNDI
    values so that the standard ValueProducer class can load them is not possible
*/
public class PropertiesFileValueProducer {

    private static final String PROPERTIES_FILENAME = "valueproducer.properties";

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesFileValueProducer.class);

    private Properties properties;

    public PropertiesFileValueProducer() throws NamingException {
        LOGGER.info("PropertiesFileValueProducer() init");
        loadProperties();
    }

    private void loadProperties() {
        properties = new Properties();

        final InputStream is = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILENAME);
        if (is != null) {
            LOGGER.info("PropertiesFileValueProducer - loading properties from {}", PROPERTIES_FILENAME);
            properties = new Properties();
            try {
                properties.load(is);
            }
            catch (IOException ioEx) {
                LOGGER.warn("IOException loading proeprties file {}", PROPERTIES_FILENAME, ioEx);
            }
        }

    }


    @Value
    @Produces
    public String stringValueOf(final InjectionPoint ip) throws NamingException {
        final Value annotation = (Value)ip.getAnnotated().getAnnotation(Value.class);
        return properties.getProperty(annotation.key(), annotation.defaultValue());
    }

 }

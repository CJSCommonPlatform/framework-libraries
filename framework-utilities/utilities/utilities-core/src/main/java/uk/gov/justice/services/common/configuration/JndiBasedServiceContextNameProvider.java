package uk.gov.justice.services.common.configuration;

import static java.lang.String.format;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;

import com.google.common.annotations.VisibleForTesting;

@ApplicationScoped
public class JndiBasedServiceContextNameProvider implements ServiceContextNameProvider {

    private static final String JAVA_APP_APP_NAME = "java:app/AppName";

    @Resource(lookup = JAVA_APP_APP_NAME)
    String appName;

    public JndiBasedServiceContextNameProvider() {
    }

    @VisibleForTesting
    public JndiBasedServiceContextNameProvider(final String appName) {
        this.appName = appName;
    }

    @Override
    public String getServiceContextName() {

        if (appName == null) {
            throw new JndiNameNotFoundException(format("No JNDI name specified for JNDI property '%s'", JAVA_APP_APP_NAME));
        }

        return appName;
    }
}
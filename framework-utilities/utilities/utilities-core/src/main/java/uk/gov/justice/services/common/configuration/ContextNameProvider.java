package uk.gov.justice.services.common.configuration;

import uk.gov.justice.services.common.util.LazyValue;

import javax.inject.Inject;

public class ContextNameProvider {

    @Inject
    private ServiceContextNameProvider serviceContextNameProvider;

    private LazyValue lazyValue = new LazyValue();

    public String getContextName() {

        return lazyValue.createIfAbsent(this::calculateContextName);
    }

    private String calculateContextName() {
        final String serviceContextName = serviceContextNameProvider.getServiceContextName();

        final String[] splits = serviceContextName.split("-");

        if (splits.length == 0) {

            return serviceContextName;
        }

        return splits[0];
    }
}

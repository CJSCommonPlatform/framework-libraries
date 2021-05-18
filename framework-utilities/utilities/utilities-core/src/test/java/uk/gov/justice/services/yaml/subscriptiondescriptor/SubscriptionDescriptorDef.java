package uk.gov.justice.services.yaml.subscriptiondescriptor;

import com.fasterxml.jackson.annotation.JsonCreator;

public class SubscriptionDescriptorDef {

    private final SubscriptionsDescriptor subscriptionsDescriptor;

    @JsonCreator
    public SubscriptionDescriptorDef(final SubscriptionsDescriptor subscriptionsDescriptor) {
        this.subscriptionsDescriptor = subscriptionsDescriptor;
    }

    public SubscriptionsDescriptor getSubscriptionsDescriptor() {
        return subscriptionsDescriptor;
    }
}


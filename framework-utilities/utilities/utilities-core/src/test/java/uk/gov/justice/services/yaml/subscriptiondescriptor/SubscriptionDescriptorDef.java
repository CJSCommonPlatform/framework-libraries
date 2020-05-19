package uk.gov.justice.services.yaml.subscriptiondescriptor;

public class SubscriptionDescriptorDef {

    private final SubscriptionsDescriptor subscriptionsDescriptor;

    public SubscriptionDescriptorDef(final SubscriptionsDescriptor subscriptionsDescriptor) {
        this.subscriptionsDescriptor = subscriptionsDescriptor;
    }

    public SubscriptionsDescriptor getSubscriptionsDescriptor() {
        return subscriptionsDescriptor;
    }
}


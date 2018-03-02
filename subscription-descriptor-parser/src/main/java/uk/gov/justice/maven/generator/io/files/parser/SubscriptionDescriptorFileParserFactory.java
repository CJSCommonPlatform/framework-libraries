package uk.gov.justice.maven.generator.io.files.parser;


import uk.gov.justice.domain.subscriptiondescriptor.SubscriptionDescriptor;

public class SubscriptionDescriptorFileParserFactory implements FileParserFactory<SubscriptionDescriptor> {

    @Override
    public SubscriptionDescriptorFileParser create() {
        return new SubscriptionDescriptorFileParser();
    }
}

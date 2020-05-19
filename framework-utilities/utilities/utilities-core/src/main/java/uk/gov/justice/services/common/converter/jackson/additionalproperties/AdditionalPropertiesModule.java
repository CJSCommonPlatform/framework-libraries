package uk.gov.justice.services.common.converter.jackson.additionalproperties;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class AdditionalPropertiesModule extends SimpleModule {

    @Override
    public void setupModule(final SetupContext context) {
        super.setupModule(context);
        context.addBeanSerializerModifier(new AdditionalPropertiesBeanSerializerModifier());
        context.addBeanDeserializerModifier(new AdditionalPropertiesBeanDeserializerModifier());
    }
}

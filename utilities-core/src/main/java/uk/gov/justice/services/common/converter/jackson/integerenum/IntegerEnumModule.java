package uk.gov.justice.services.common.converter.jackson.integerenum;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class IntegerEnumModule extends SimpleModule {

    @Override
    public void setupModule(final SetupContext context) {
        super.setupModule(context);
        context.addBeanSerializerModifier(new IntegerEnumBeanSerializerModifier());
        context.addBeanDeserializerModifier(new IntegerEnumBeanDeserializerModifier());
    }

}

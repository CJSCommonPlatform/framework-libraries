package uk.gov.justice.raml.jaxrs.core;

import static com.sun.codemodel.JMod.PUBLIC;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

import uk.gov.justice.services.adapter.rest.RestProcessor;
import uk.gov.justice.services.adapter.rest.builder.UnmodifiableMapBuilder;
import uk.gov.justice.services.core.annotation.Adapter;
import uk.gov.justice.services.core.annotation.Component;
import uk.gov.justice.services.core.dispatcher.Dispatcher;

public class JaxRsResourceImplementationCodeGenerator {
    private final JCodeModel codeModel;

    public JaxRsResourceImplementationCodeGenerator(JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    public JDefinedClass createImplementation(JDefinedClass resourceInterface)
            throws JClassAlreadyExistsException {
        final JPackage pkg = resourceInterface.getPackage();
        final JDefinedClass resourceImplementation = pkg._class("Default" + resourceInterface.name());
        resourceImplementation._implements(resourceInterface);
        addAnnotations(resourceImplementation);
        addImplemetationMethods(resourceImplementation, resourceInterface);

        addAnnotatedProperty(resourceImplementation, Dispatcher.class, "dispatcher", Inject.class);
        addAnnotatedProperty(resourceImplementation, RestProcessor.class, "restProcessor", Inject.class);
        addAnnotatedProperty(resourceImplementation, HttpHeaders.class, "headers", javax.ws.rs.core.Context.class);
        return resourceImplementation;
    }

    private void addAnnotations(final JDefinedClass resourceImplementation) {
        resourceImplementation.annotate(Stateless.class);
        JAnnotationUse adapterAnnotation = resourceImplementation.annotate(Adapter.class);
        adapterAnnotation.param("value", Component.COMMAND_API);
    }

    private void addImplemetationMethods(final JDefinedClass resourceImplementation, JDefinedClass resourceInterface) {
        JClass str = codeModel.ref("String");
        resourceInterface.methods().forEach(interfaceMethod -> {
            JMethod implementationMethod = resourceImplementation.method(PUBLIC, interfaceMethod.type(),
                    interfaceMethod.name());
            implementationMethod.annotate(Override.class);

            addMethodParams(implementationMethod, interfaceMethod);

            JType map = codeModel.ref(Map.class.getCanonicalName()).narrow(str, str);
            JClass mapBuilderClass = codeModel.ref(UnmodifiableMapBuilder.class.getCanonicalName()).narrow(str, str);

            JBlock body = implementationMethod.body();
            body.decl(map, "pathParams", pathParamsMapBuilderInvocation(mapBuilderClass, implementationMethod));
            body.directStatement("return restProcessor.process(dispatcher::dispatch, entity, headers, pathParams);");
        });
    }

    private void addAnnotatedProperty(final JDefinedClass resourceClass, Class<?> clazz, String name,
            Class<? extends Annotation> annotation) {
        JFieldVar dispatcherField = resourceClass.field(0, clazz, name);
        dispatcherField.annotate(annotation);
    }

    private void addMethodParams(JMethod implMethod, JMethod m) {
        m.params().forEach(p -> implMethod.param(p.type(), p.name()));
    }

    private JInvocation pathParamsMapBuilderInvocation(JClass mapBuilderClass, JMethod implMethod) {
        JInvocation builderInstance = JExpr._new(mapBuilderClass);
        List<JVar> params = implMethod.params();
        for (int i = 0; i < params.size() - 1; i++) {
            JVar p = params.get(i);
            builderInstance = builderInstance.invoke("with").arg(JExpr.lit(p.name())).arg(p);
        }
        return builderInstance.invoke("build");
    }

}

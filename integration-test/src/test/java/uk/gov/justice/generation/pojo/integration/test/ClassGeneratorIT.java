package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.bootstrap.GenerationContextFactory;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.integration.utils.ClassCompiler;
import uk.gov.justice.generation.pojo.write.SourceWriter;
import uk.gov.justice.services.common.converter.ZonedDateTimes;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassGeneratorIT {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final ClassCompiler classCompiler = new ClassCompiler();

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void shouldGenerateJavaClassSourceCode() throws Exception {

        final String packageName = "uk.gov.justice.pojo";

        final ClassDefinition addressDefinition = addressDefinition(packageName);
        final ClassDefinition employeeDefinition = employeeDefinition(packageName, addressDefinition);
        final JavaGeneratorFactory javaGeneratorFactory = new JavaGeneratorFactory();

        final List<ClassGeneratable> classGenerators = asList(
                javaGeneratorFactory.createClassGeneratorFor(addressDefinition),
                javaGeneratorFactory.createClassGeneratorFor(employeeDefinition)
        );

        final File sourceOutputDirectory = new File("./target/test-generation");
        final File classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        final GenerationContext generationContext = new GenerationContextFactory().create(sourceOutputDirectory);

        sourceOutputDirectory.delete();

        final List<? extends Class<?>> classes = classGenerators.stream().map(classGenerator -> {
            sourceWriter.write(classGenerator, generationContext);
            return classCompiler.compile(classGenerator, sourceOutputDirectory, classesOutputDirectory);
        }).collect(toList());

        assertThat(classes.get(0).getName(), is(addressDefinition.getClassName().getFullyQualifiedName()));
        assertThat(classes.get(1).getName(), is(employeeDefinition.getClassName().getFullyQualifiedName()));

        final String firstName = "firstName";
        final String lastName = "lastName";
        final BigDecimal poundsPerHour = new BigDecimal("5.99");
        final ZonedDateTime startDate = ZonedDateTimes.fromString("2016-03-18T00:46:54.700Z");
        final List<String> favouriteColours = asList("red", "blue");

        final String firstLine = "firstLine";
        final String postCode = "postCode";

        final Constructor<?> addressConstructor = classes.get(0).getConstructor(String.class, String.class);
        final Object address = addressConstructor.newInstance(firstLine, postCode);

        final Constructor<?> employeeConstructor = classes.get(1).getConstructor(String.class, String.class, BigDecimal.class, ZonedDateTime.class, List.class, address.getClass());
        final Object employee = employeeConstructor.newInstance(firstName, lastName, poundsPerHour, startDate, favouriteColours, address);

        final String employeeJson = objectMapper.writeValueAsString(employee);

        with(employeeJson)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.poundsPerHour", is(poundsPerHour.doubleValue()))
                .assertThat("$.startDate", is("2016-03-18T00:46:54.700Z"))
                .assertThat("$.favouriteColours[0]", is("red"))
                .assertThat("$.favouriteColours[1]", is("blue"))
                .assertThat("$.address.firstLine", is(firstLine))
                .assertThat("$.address.postCode", is(postCode))
        ;
    }

    private ClassDefinition addressDefinition(final String packageName) {
        final ClassDefinition addressDefinition = new ClassDefinition("address", new ClassName(packageName, "Address"));
        addressDefinition.addFieldDefinition(new FieldDefinition("firstLine", new ClassName(String.class)));
        addressDefinition.addFieldDefinition(new FieldDefinition("postCode", new ClassName(String.class)));

        return addressDefinition;
    }

    private ClassDefinition employeeDefinition(final String packageName, final ClassDefinition addressDefinition) {
        final ClassDefinition employeeDefinition = new ClassDefinition("employee", new ClassName(packageName, "Employee"));
        employeeDefinition.addFieldDefinition(new FieldDefinition("firstName", new ClassName(String.class)));
        employeeDefinition.addFieldDefinition(new FieldDefinition("lastName", new ClassName(String.class)));
        employeeDefinition.addFieldDefinition(new FieldDefinition("poundsPerHour", new ClassName(BigDecimal.class)));
        employeeDefinition.addFieldDefinition(new FieldDefinition("startDate", new ClassName(ZonedDateTime.class)));
        employeeDefinition.addFieldDefinition(new FieldDefinition("favouriteColours", new ClassName(List.class), new ClassName(String.class)));
        employeeDefinition.addFieldDefinition(addressDefinition);

        return employeeDefinition;
    }
}

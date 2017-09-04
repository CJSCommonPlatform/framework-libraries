package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ARRAY;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.NUMBER;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.dom.StringDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.plugin.DefaultPluginProvider;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassGeneratorIT {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final ClassCompiler classCompiler = new ClassCompiler();

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

    private File sourceOutputDirectory;
    private File classesOutputDirectory;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File("./target/test-generation/class-generator");
        classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    @Test
    public void shouldGenerateJavaClassSourceCode() throws Exception {

        final String packageName = "uk.gov.justice.pojo.classgenerator";

        final GenerationContext generationContext = new GenerationContext(sourceOutputDirectory.toPath(), packageName);
        final ClassDefinition addressDefinition = addressDefinition();
        final ClassDefinition employeeDefinition = employeeDefinition(addressDefinition);

        final JavaGeneratorFactory javaGeneratorFactory = new JavaGeneratorFactory(new ClassNameFactory(packageName));

        final List<? extends Class<?>> classes = javaGeneratorFactory
                .createClassGeneratorsFor(asList(addressDefinition, employeeDefinition), new DefaultPluginProvider())
                .stream()
                .map(classGenerator -> {
                    sourceWriter.write(classGenerator, generationContext);
                    return classCompiler.compile(classGenerator, generationContext, classesOutputDirectory);
                }).collect(toList());

        final String firstName = "firstName";
        final String lastName = "lastName";
        final BigDecimal poundsPerHour = new BigDecimal("5.99");
        final ZonedDateTime startDate = ZonedDateTimes.fromString("2016-03-18T00:46:54.700Z");
        final List<String> favouriteColours = asList("red", "blue");

        final String firstLine = "firstLine";
        final String postCode = "postCode";

        final Constructor<?> addressConstructor = classes.get(0).getConstructor(String.class, String.class);
        final Object address = addressConstructor.newInstance(firstLine, postCode);

        final Constructor<?> employeeConstructor = classes.get(1).getConstructor(address.getClass(), List.class, String.class, String.class, BigDecimal.class, ZonedDateTime.class);
        final Object employee = employeeConstructor.newInstance(address, favouriteColours, firstName, lastName, poundsPerHour, startDate);

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

    private ClassDefinition addressDefinition() {
        final ClassDefinition addressDefinition = new ClassDefinition(CLASS, "address");
        addressDefinition.addFieldDefinition(new StringDefinition("firstLine", null));
        addressDefinition.addFieldDefinition(new StringDefinition("postCode", null));

        return addressDefinition;
    }

    private ClassDefinition employeeDefinition(final ClassDefinition addressDefinition) {
        final ClassDefinition employeeDefinition = new ClassDefinition(CLASS, "employee");
        employeeDefinition.addFieldDefinition(new StringDefinition("firstName", null));
        employeeDefinition.addFieldDefinition(new StringDefinition("lastName", null));
        employeeDefinition.addFieldDefinition(new FieldDefinition(NUMBER, "poundsPerHour"));
        employeeDefinition.addFieldDefinition(new StringDefinition("startDate", "ZonedDateTime"));
        employeeDefinition.addFieldDefinition(favouriteColoursDefinition());
        employeeDefinition.addFieldDefinition(addressDefinition);

        return employeeDefinition;
    }

    private ClassDefinition favouriteColoursDefinition() {
        final ClassDefinition favouriteColours = new ClassDefinition(ARRAY, "favouriteColours");
        favouriteColours.addFieldDefinition(new StringDefinition("favouriteColours", "none"));

        return favouriteColours;
    }
}

package uk.gov.justice.generation.pojo.write;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JavaClassFileWriterTest {

    @Mock
    private NonDuplicatingSourceWriter nonDuplicatingSourceWriter;

    @InjectMocks
    private JavaClassFileWriter javaClassFileWriter;

    @Test
    @SuppressWarnings("unchecked")
    public void shouldWriteJavaClassFiles() throws Exception {
        final GenerationContext generationContext = mock(GenerationContext.class);
        final ClassGeneratable classGeneratable = mock(ClassGeneratable.class);
        final List<ClassGeneratable> classGenerators = singletonList(classGeneratable);

        javaClassFileWriter.writeJavaClassesToFile(generationContext, classGenerators);

        verify(nonDuplicatingSourceWriter, times(1)).write(classGeneratable, generationContext);
    }
}

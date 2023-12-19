package uk.gov.justice.services.fileservice.repository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileContentTest {

    @Test
    public void shouldCloseInputStreamOnClose() throws Exception {

        final InputStream content = mock(InputStream.class);
        final FileContent fileContent = new FileContent(content);

        fileContent.close();

        verify(content).close();
    }
}
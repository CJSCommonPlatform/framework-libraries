package uk.gov.justice.services.fileservice.io;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class InputStreamWrapperTest {

    @Mock
    private InputStream inputStream;

    @Mock
    private Connection connection;

    @InjectMocks
    private InputStreamWrapper inputStreamWrapper;

    @Test
    public void shouldCloseInputStreamConnectionPreparedStatementAndResultSetOnClose() throws Exception {

        inputStreamWrapper.close();

        final InOrder inOrder = inOrder(
                inputStream,
                connection
        );

        inOrder.verify(inputStream).close();
        inOrder.verify(connection).close();
    }

    @Test
    public void shouldCloseAllEvenIfTheInputStreamThrowsAnException() throws Exception {

        final IOException ioException = new IOException("Ooops");

        doThrow(ioException).when(inputStream).close();

        try {
            inputStreamWrapper.close();
            fail();
        } catch (IOException expected) {
            assertThat(expected.getMessage(), startsWith("Failed to close InputStream"));
            assertThat(expected.getCause(), is(ioException));
        }

        verify(connection).close();
    }

    @Test
    public void shouldCloseAllEvenIfTheConnectionThrowsAnException() throws Exception {

        final SQLException sqlException = new SQLException("Ooops");

        doThrow(sqlException).when(connection).close();

        try {
            inputStreamWrapper.close();
            fail();
        } catch (IOException expected) {
            assertThat(expected.getMessage(), startsWith("Failed to close Connection"));
            assertThat(expected.getCause(), is(sqlException));
        }

        verify(inputStream).close();
    }

    @Test
    public void shouldWrapTheCallsToRead() throws Exception {

        final int bytesRead_1 = 239847;
        final int bytesRead_2 = 934879;
        final int bytesRead_3 = 734972;

        final byte[] bytes = "some bytes".getBytes();
        final int offset = 23;
        final int length = bytes.length;

        when(inputStream.read()).thenReturn(bytesRead_1);
        when(inputStream.read(bytes)).thenReturn(bytesRead_2);
        when(inputStream.read(bytes, offset, length)).thenReturn(bytesRead_3);

       assertThat(inputStreamWrapper.read(), is(bytesRead_1));
       assertThat(inputStreamWrapper.read(bytes), is(bytesRead_2));
       assertThat(inputStreamWrapper.read(bytes, offset, length), is(bytesRead_3));
    }

    @Test
    public void shouldWrapTheCallToSkip() throws Exception {

        final long bytesToSkip = 3273454923L;
        final long bytesSkipped = 97324987234L;

        when(inputStream.skip(bytesToSkip)).thenReturn(bytesSkipped);

        assertThat(inputStreamWrapper.skip(bytesToSkip), is(bytesSkipped));
    }

    @Test
    public void shouldWrapTheCallToAvailable() throws Exception {

        final int bytesAvailable = 3948;

        when(inputStream.available()).thenReturn(bytesAvailable);

        assertThat(inputStreamWrapper.available(), is(bytesAvailable));
    }
    @Test
    public void shouldWrapTheCallToMark() throws Exception {

        final int readLimit = 983724;

        inputStreamWrapper.mark(readLimit);

        verify(inputStream).mark(readLimit);
    }


    @Test
    public void shouldWrapTheCallToReset() throws Exception {

        inputStreamWrapper.reset();

        verify(inputStream).reset();
    }

    @Test
    public void shouldWrapTheCallToMarkSupported() throws Exception {

        final boolean markSupported = true;

        when(inputStreamWrapper.markSupported()).thenReturn(markSupported);

        assertThat(inputStream.markSupported(), is(markSupported));
    }

    @Test
    public void shouldTryToGetBetterCodeCoverageByTestingUselessMethods() throws Exception {
        assertThat(inputStreamWrapper.getConnection(), is(connection));
        assertThat(inputStreamWrapper.getInputStream(), is(inputStream));
    }
}

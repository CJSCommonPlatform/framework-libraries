package uk.gov.justice.generation.pojo.integration.utils;

import static java.nio.charset.Charset.defaultCharset;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class FileLoader {

    public String loadAsJsonString(final String pathname) {
        return loadAsJsonString(new File(pathname));
    }

    public String loadAsJsonString(final File file) {
        try(final FileInputStream input = new FileInputStream(file)) {
            return IOUtils.toString(input, defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file " + file.getAbsolutePath(), e);
        }
    }
}

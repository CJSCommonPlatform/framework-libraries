package uk.gov.justice;

import static java.lang.String.format;
import static java.nio.file.Paths.get;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public abstract class AbstractTestHelper {

    public static String getFileContents(String path) throws Exception
    {

       String parent = AbstractTestHelper.class.getResource("/").getPath();
       File file = new File(parent, path);
        return FileUtils.readFileToString(file);
    }

    public static File getFile(String path) throws Exception
    {

        String parent = AbstractTestHelper.class.getResource("/").getPath();
        File file = new File(parent, path);
      return file;
    }

    public static String getFilepath(String path) throws Exception
    {

        String parent = AbstractTestHelper.class.getResource("/").getPath();
        File file = new File(parent, path);
        return file.getAbsolutePath();
    }
}

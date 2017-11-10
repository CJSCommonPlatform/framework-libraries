package uk.gov.justice.schema.catalog;

import java.io.File;

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

package uk.gov.justice.plugin.report;

import org.slf4j.Logger;
import uk.gov.justice.plugin.domain.ReportConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.IOUtils.write;


public abstract class AbstractValidationReport implements ValidationReport {

    private static final String PLUGIN_VALIDATION_RESULT_PREFIX = "annotation-plugin-validation-result-";

    @Override
    public void generateReport(final ReportConfig reportConfig) {
        final String filename = reportConfig.getReportTargetDirectory() + File.separator + PLUGIN_VALIDATION_RESULT_PREFIX + reportConfig.getAnnotationClass() + ".csv";
        getLogger().debug("Deleting previously created file (if present) - '{}'", filename);
        deleteQuietly(new File(filename));

        getLogger().info("Writing report to '{}'", filename);

        try {
            write(getFileContents(reportConfig).getBytes(), new FileOutputStream(filename));
        } catch (IOException e) {
            getLogger().error("Unable to write report to file: '{}'", filename);
        }
    }

    protected abstract String getFileContents(ReportConfig reportConfig);

    protected abstract Logger getLogger();
}

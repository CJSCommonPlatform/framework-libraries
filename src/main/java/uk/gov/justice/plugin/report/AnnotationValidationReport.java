package uk.gov.justice.plugin.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.justice.plugin.domain.ReportConfig;

import java.util.List;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Lists.newArrayList;


public class AnnotationValidationReport extends AbstractValidationReport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationValidationReport.class);

    @Override
    protected String getFileContents(final ReportConfig reportConfig) {

        List<String> csvRows = newArrayList();
        reportConfig.getValidationResults().forEach(vr -> {
            csvRows.add(on(",").join(new Object[]{vr.getClassWithAnnotation(), vr.getValidationText(), vr.isValidationPassed()}));
        });

        return on("\n").join(csvRows);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}

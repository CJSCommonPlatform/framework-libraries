package uk.gov.justice.plugin.report;


import uk.gov.justice.plugin.domain.ReportConfig;

public interface ValidationReport {

    void generateReport(ReportConfig reportConfig);
}

package uk.gov.justice.plugin.domain;

import java.util.List;

public class ReportConfig {

    private String annotationClass;
    private String reportTargetDirectory;
    private List<ValidationResult> validationResults;

    public ReportConfig(final String annotationClass, final String reportTargetDirectory, final List<ValidationResult> validationResults) {
        this.annotationClass = annotationClass;
        this.reportTargetDirectory = reportTargetDirectory;
        this.validationResults = validationResults;
    }

    public String getAnnotationClass() {
        return annotationClass;
    }

    public void setAnnotationClass(final String annotationClass) {
        this.annotationClass = annotationClass;
    }

    public String getReportTargetDirectory() {
        return reportTargetDirectory;
    }

    public void setReportTargetDirectory(final String reportTargetDirectory) {
        this.reportTargetDirectory = reportTargetDirectory;
    }

    public List<ValidationResult> getValidationResults() {
        return validationResults;
    }

    public void setValidationResults(final List<ValidationResult> validationResults) {
        this.validationResults = validationResults;
    }
}

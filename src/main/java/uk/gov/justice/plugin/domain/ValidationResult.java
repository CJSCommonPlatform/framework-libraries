package uk.gov.justice.plugin.domain;

public class ValidationResult {

    private String classWithAnnotation;
    private boolean validationPassed;
    private String validationText;

    public ValidationResult(final String classWithAnnotation, final boolean validationPassed, final String validationText) {
        this.classWithAnnotation = classWithAnnotation;
        this.validationPassed = validationPassed;
        this.validationText = validationText;
    }

    public String getClassWithAnnotation() {
        return classWithAnnotation;
    }

    public void setClassWithAnnotation(final String classWithAnnotation) {
        this.classWithAnnotation = classWithAnnotation;
    }

    public boolean isValidationPassed() {
        return validationPassed;
    }

    public void setValidationPassed(final boolean validationPassed) {
        this.validationPassed = validationPassed;
    }

    public String getValidationText() {
        return validationText;
    }

    public void setValidationText(final String validationText) {
        this.validationText = validationText;
    }
}

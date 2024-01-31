package uk.gov.moj.cpp.task.execution;

public class RandomPercentageProvider {

    /**
     * Gets a pseudo random integer from zero (inclusive) to 100 (exclusive)
     *
     * @return An Integer greater or equal to zero and less than 100
     */
    @SuppressWarnings("java:S2245") // suppress the java hotspot warning that Math.random() is not cryptographically secure. We don't care.
    public int getRandomPercentage() {
        return (int) Math.floor(Math.random() * 100d);
    }
}

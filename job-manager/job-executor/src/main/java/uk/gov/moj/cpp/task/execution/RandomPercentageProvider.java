package uk.gov.moj.cpp.task.execution;

import java.security.SecureRandom;

public class RandomPercentageProvider {

    /**
     * Gets a pseudo random integer from zero (inclusive) to 100 (exclusive)
     *
     * @return An Integer greater or equal to zero and less than 100
     */
    public int getRandomPercentage() {
        return new SecureRandom().nextInt(100);
    }
}

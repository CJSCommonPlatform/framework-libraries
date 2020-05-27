package uk.gov.moj.cpp.jobmanager.example.task.data;


public class CakeBakingTime {

    private final int cookingTimeSeconds;
    private final String startTimeString;

    public CakeBakingTime(final int cookingTimeSeconds, final String startTimeString) {
        this.cookingTimeSeconds = cookingTimeSeconds;
        this.startTimeString = startTimeString;
    }

    public int getCookingTimeSeconds() {
        return cookingTimeSeconds;
    }

    public String getStartTimeString() {
        return startTimeString;
    }

}

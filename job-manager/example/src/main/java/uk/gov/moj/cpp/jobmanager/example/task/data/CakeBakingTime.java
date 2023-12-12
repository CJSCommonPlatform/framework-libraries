package uk.gov.moj.cpp.jobmanager.example.task.data;


public record CakeBakingTime(int cookingTimeSeconds, String startTimeString) {

    public int getCookingTimeSeconds() {
        return cookingTimeSeconds;
    }

    public String getStartTimeString() {
        return startTimeString;
    }
}

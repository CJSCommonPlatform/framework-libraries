package uk.gov.moj.cpp.jobmanager.example.task.data;

public record OvenSettings(int degreesCelsius, int shelfNumber, boolean useSteamFunction) {

    public int getDegreesCelsius() {
        return degreesCelsius;
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public boolean isUseSteamFunction() {
        return useSteamFunction;
    }
}

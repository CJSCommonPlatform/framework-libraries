package uk.gov.moj.cpp.jobmanager.example.task.data;

public class OvenSettings {

    private final int degreesCelsius;
    private final int shelfNumber;
    private final boolean useSteamFunction;

    public OvenSettings(final int degreesCelsius, final int shelfNumber, final boolean useSteamFunction) {
        this.degreesCelsius = degreesCelsius;
        this.shelfNumber = shelfNumber;
        this.useSteamFunction = useSteamFunction;
    }

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

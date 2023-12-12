package uk.gov.moj.cpp.jobmanager.example.task.data;

public record SliceCake(int numberOfSlices) {

    public int getNumberOfSlices() {
        return numberOfSlices;
    }
}

package uk.gov.moj.cpp.jobmanager.example.task.data;

import java.util.List;

public class Utensils {

    private final List<String> utensilsList;

    public Utensils(final List<String> utensilsList) {
        this.utensilsList = utensilsList;
    }


    public List<String> getUtensilsList() {
        return utensilsList;
    }
}

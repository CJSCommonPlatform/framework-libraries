package uk.gov.moj.cpp.jobmanager.example.task.data;

import java.util.List;

public record Utensils(List<String> utensilsList) {

    public List<String> getUtensilsList() {
        return utensilsList;
    }
}

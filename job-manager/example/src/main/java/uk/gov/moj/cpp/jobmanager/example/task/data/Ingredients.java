package uk.gov.moj.cpp.jobmanager.example.task.data;

import java.util.List;

public record Ingredients(List<String> ingredientList) {

    public List<String> getIngredientList() {
        return ingredientList;
    }
}

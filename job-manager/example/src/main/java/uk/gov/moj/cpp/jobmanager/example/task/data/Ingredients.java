package uk.gov.moj.cpp.jobmanager.example.task.data;

import java.util.List;

public class Ingredients {

    private final List<String> ingredientList;

    public Ingredients(final List<String> ingredientList) {
        this.ingredientList = ingredientList;
    }


    public List<String> getIngredientList() {
        return ingredientList;
    }
}

package uk.gov.moj.cpp.jobmanager.example;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

import uk.gov.moj.cpp.jobmanager.example.task.data.CakeBakingTime;
import uk.gov.moj.cpp.jobmanager.example.task.data.Ingredients;
import uk.gov.moj.cpp.jobmanager.example.task.data.Instruction;
import uk.gov.moj.cpp.jobmanager.example.task.data.OvenSettings;
import uk.gov.moj.cpp.jobmanager.example.task.data.SliceCake;
import uk.gov.moj.cpp.jobmanager.example.task.data.Utensils;

/**
 * Simple enum to encapsulate workflow steps, ordering and associated step/task data
 */
public enum MakeCakeWorkflow {

    SWITCH_OVEN_ON(new OvenSettings(210, 2, true)),
    GET_INGREDIENTS(new Ingredients(asList("250g plain flour", "125g butter", "1Tbsp baking powder", "100g sugar", "2 eggs"))),
    GET_UTENSILS(new Utensils(asList("Large mixing bowl", "Fine sieve", "Large wooden spoon", "Whisk", "Cake Tin", "Pallette knife", "Icing bag"))),
    MIX_INGREDIENTS(new Instruction(asList("Put all ingredients in bowl", "Mix ingredients until you have a smooth consistency"))),
    FILL_CAKE_TIN(new Instruction(asList("Line cake tin with greaseproof paper", "Pour cake mixture into cake tin"))),
    BAKE_CAKE(new CakeBakingTime(5, null)),
    CAKE_MADE(new SliceCake(6));

    private Object taskData;

    MakeCakeWorkflow(Object taskData) {
        this.taskData = taskData;
    }

    public static MakeCakeWorkflow firstTask() {
        return SWITCH_OVEN_ON;
    }


    public static MakeCakeWorkflow nextTask(final MakeCakeWorkflow lastTaskPerformed){

        return stream(values()).filter(e -> e.ordinal() >  lastTaskPerformed.ordinal())
                               .findFirst()
                               .orElse(CAKE_MADE);

    }


    public Object getTaskData() {
        return taskData;
    }

}

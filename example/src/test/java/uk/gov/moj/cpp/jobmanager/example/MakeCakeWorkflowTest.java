package uk.gov.moj.cpp.jobmanager.example;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.gov.moj.cpp.jobmanager.example.MakeCakeWorkflow.BAKE_CAKE;
import static uk.gov.moj.cpp.jobmanager.example.MakeCakeWorkflow.CAKE_MADE;
import static uk.gov.moj.cpp.jobmanager.example.MakeCakeWorkflow.FILL_CAKE_TIN;
import static uk.gov.moj.cpp.jobmanager.example.MakeCakeWorkflow.GET_INGREDIENTS;
import static uk.gov.moj.cpp.jobmanager.example.MakeCakeWorkflow.GET_UTENSILS;
import static uk.gov.moj.cpp.jobmanager.example.MakeCakeWorkflow.MIX_INGREDIENTS;
import static uk.gov.moj.cpp.jobmanager.example.MakeCakeWorkflow.SWITCH_OVEN_ON;

import org.junit.Test;

public class MakeCakeWorkflowTest {

    @Test
    public void shouldReturnFirstTask() {
        assertThat(MakeCakeWorkflow.firstTask(), is(SWITCH_OVEN_ON));
    }

    @Test
    public void shouldReturnNextTask() {

        assertThat(MakeCakeWorkflow.nextTask(SWITCH_OVEN_ON), is(GET_INGREDIENTS));
        assertThat(MakeCakeWorkflow.nextTask(GET_INGREDIENTS), is(GET_UTENSILS));
        assertThat(MakeCakeWorkflow.nextTask(GET_UTENSILS), is(MIX_INGREDIENTS));
        assertThat(MakeCakeWorkflow.nextTask(MIX_INGREDIENTS), is(FILL_CAKE_TIN));
        assertThat(MakeCakeWorkflow.nextTask(FILL_CAKE_TIN), is(BAKE_CAKE));
        assertThat(MakeCakeWorkflow.nextTask(BAKE_CAKE), is(CAKE_MADE));
        assertThat(MakeCakeWorkflow.nextTask(CAKE_MADE), is(CAKE_MADE));

    }
}

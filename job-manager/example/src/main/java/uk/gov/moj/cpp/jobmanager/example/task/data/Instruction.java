package uk.gov.moj.cpp.jobmanager.example.task.data;

import java.util.List;

public class Instruction {

    private final List<String> instructionList;

    public Instruction(final List<String> instructionList) {
        this.instructionList = instructionList;
    }


    public List<String> getInstructionList() {
        return instructionList;
    }
}

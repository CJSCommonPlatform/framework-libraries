package uk.gov.moj.cpp.jobmanager.example.task.data;

import java.util.List;

public record Instruction(List<String> instructionList) {

    public List<String> getInstructionList() {
        return instructionList;
    }
}

package thkoeln.coco.ad.instruction;

import lombok.Getter;

@Getter
public class MoveInstruction extends Instruction{
    private final Integer steps;

    public MoveInstruction(String instruction, Integer steps) {
        this.instruction = instruction;
        this.steps = steps;
    }

}

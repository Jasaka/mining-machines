package thkoeln.coco.ad.instruction;

import lombok.Getter;

@Getter
public class MoveInstruction extends Instruction{

    private final String direction;
    private final Integer steps;

    public MoveInstruction(String inputString) {
        String[] splitString = inputString.replaceAll("[\\[\\]]", "").split(",");
        this.direction = splitString[0];
        this.steps = Integer.parseInt(splitString[1]);
    }
}

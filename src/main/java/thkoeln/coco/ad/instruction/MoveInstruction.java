package thkoeln.coco.ad.instruction;

import lombok.Getter;
import thkoeln.coco.ad.primitive.Direction;


@Getter
public class MoveInstruction extends Instruction{

    private final Direction direction;
    private final Integer steps;

    public MoveInstruction(String inputString) {
        String[] splitString = inputString.replaceAll("[\\[\\]]", "").split(",");
        this.direction = Direction.valueOf(splitString[0].toUpperCase());
        this.steps = Integer.parseInt(splitString[1]);
    }
}

package thkoeln.coco.ad.instruction;

import lombok.Getter;

@Getter
public class BarrierInstruction extends Instruction{
    private final String startSquare;
    private final String endSquare;

    public BarrierInstruction(String instruction, String startSquare, String endSquare) {
        this.instruction = instruction;
        this.startSquare = startSquare;
        this.endSquare = endSquare;
    }
}

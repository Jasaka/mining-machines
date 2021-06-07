package thkoeln.coco.ad.instruction;

import lombok.Getter;

import java.util.UUID;
@Getter
public class TransportInstruction extends Instruction{
    private final UUID targetedId;

    public TransportInstruction(String instruction, UUID targetedId) {
        this.instruction = instruction;
        this.targetedId = targetedId;
    }

}

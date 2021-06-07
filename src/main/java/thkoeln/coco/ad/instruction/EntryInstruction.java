package thkoeln.coco.ad.instruction;

import lombok.Getter;

import java.util.UUID;

@Getter
public class EntryInstruction extends Instruction{
    private final UUID targetedId;

    public EntryInstruction(String instruction, UUID targetedId) {
        this.instruction = instruction;
        this.targetedId = targetedId;
    }

}

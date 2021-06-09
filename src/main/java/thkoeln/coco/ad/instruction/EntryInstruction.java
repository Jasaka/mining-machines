package thkoeln.coco.ad.instruction;

import lombok.Getter;

import java.util.UUID;

@Getter
public class EntryInstruction extends Instruction{

    private final UUID targetedFieldId;

    public EntryInstruction(String inputString) {
        this.targetedFieldId = UUID.fromString(inputString.replaceAll("\\[en,", "").replaceAll("\\]", ""));
    }
}

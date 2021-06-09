package thkoeln.coco.ad.instruction;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TransportInstruction extends Instruction{

    private final UUID targetedFieldId;

    public TransportInstruction(String inputString) {
        this.targetedFieldId = UUID.fromString(inputString.replaceAll("\\[tr,", "").replaceAll("\\]", ""));
    }
}

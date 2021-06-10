package thkoeln.coco.ad.miningMachine;

import lombok.Getter;
import lombok.Setter;
import thkoeln.coco.ad.field.Square;
import thkoeln.coco.ad.instruction.EntryInstruction;
import thkoeln.coco.ad.instruction.Instruction;
import thkoeln.coco.ad.instruction.MoveInstruction;
import thkoeln.coco.ad.instruction.TransportInstruction;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

@Entity
public class MiningMachine {

    @Id
    @Getter
    private final UUID id = UUID.randomUUID();

    @Getter
    @Setter
    private String name;

    @Getter
    @OneToOne
    private Square currentSquare = null;

    protected MiningMachine() {
    }

    public MiningMachine(String name) {
        this.name = name;
    }

    public <T extends Instruction> boolean executeInstruction(T instruction) {
        if (instruction instanceof MoveInstruction) {
            return executeMoveInstruction((MoveInstruction) instruction);
        }
        if (instruction instanceof TransportInstruction) {
            return executeTransportInstruction((TransportInstruction) instruction);
        }
        if (instruction instanceof EntryInstruction) {
            return executeEntryInstruction((EntryInstruction) instruction);
        }
        throw new MiningMachineException("No valid machine instruction provided");
    }

    private boolean executeMoveInstruction(MoveInstruction instruction) {
        return false;
    }

    private boolean executeTransportInstruction(TransportInstruction instruction) {
        return false;
    }

    private boolean executeEntryInstruction(EntryInstruction instruction) {
        if (currentSquare == null) {

            return true;
        } else return false;
    }
}

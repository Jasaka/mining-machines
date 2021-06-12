package thkoeln.coco.ad.miningMachine;

import lombok.Getter;
import lombok.Setter;
import thkoeln.coco.ad.field.Field;
import thkoeln.coco.ad.field.Square;
import thkoeln.coco.ad.instruction.EntryInstruction;
import thkoeln.coco.ad.instruction.Instruction;
import thkoeln.coco.ad.instruction.MoveInstruction;
import thkoeln.coco.ad.instruction.TransportInstruction;
import thkoeln.coco.ad.primitive.Coordinate;
import thkoeln.coco.ad.primitive.Direction;

import javax.persistence.*;
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
    @Embedded
    private Coordinate currentPosition = null;

    @Getter
    @ManyToOne
    private Field currentField = null;

    protected MiningMachine() {
    }

    public MiningMachine(String name) {
        this.name = name;
    }

    /*public <T extends Instruction> boolean executeInstruction(T instruction) {
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
    }*/

    public boolean executeMoveInstruction(MoveInstruction instruction) {
        if (this.currentField == null) {
            throw new MiningMachineException("Tried to move Mining Machine without first entering onto a field");
        }
        switch (instruction.getDirection()) {
            case NO:
                for (int i = 0; i < instruction.getSteps(); i++) {
                    if (currentPosition.getWithAddedX(1).getX() < currentField.getHeight()) {
                        if (!currentField.hasHorizontalBlockage(currentPosition.getWithAddedY(1), Direction.NO)) {

                        } else return true;
                    } else return true;
                }
                break;
            case SO:
                for (int i = 0; i < instruction.getSteps(); i++) {
                    if (currentPosition.getWithAddedY(-1).getY() >= 0) {
                        if (!currentField.hasHorizontalBlockage(currentPosition.getWithAddedY(-1), Direction.NO)) {

                        } else return true;
                    } else return true;
                }
                break;
            case EA:
                for (int i = 0; i < instruction.getSteps(); i++) {
                    if (currentPosition.getWithAddedX(1).getX() < currentField.getWidth()) {
                        if (!currentField.hasHorizontalBlockage(currentPosition.getWithAddedX(1), Direction.NO)) {

                        } else return true;
                    } else return true;
                }
                break;
            case WE:
                for (int i = 0; i < instruction.getSteps(); i++) {
                    if (currentPosition.getWithAddedX(-1).getX() >= 0) {
                        if (!currentField.hasHorizontalBlockage(currentPosition.getWithAddedX(-1), Direction.NO)) {

                        } else return true;
                    } else return true;
                }
                break;
            default:
                throw new MiningMachineException("I don't know how, but you managed to provide invalid movement instructions...");
        }
        return true;
    }

    public boolean executeTransportInstruction(TransportInstruction instruction) {
        return false;
    }

    public boolean executeEntryInstruction(Field entryField) {
        if (currentPosition == null) {
            if (entryField.getEntrySquare().getBlockedByMachine()) {
                return false;
            } else {
                this.currentPosition = new Coordinate(0, 0);
                this.currentField = entryField;
                return true;
            }
        } else return false;
    }
}

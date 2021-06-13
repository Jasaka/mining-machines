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

    public boolean executeMoveInstruction(MoveInstruction instruction) {
        if (this.currentField == null) {
            throw new MiningMachineException("Tried to move Mining Machine without first entering onto a field");
        }
        switch (instruction.getDirection()) {
            case NO:
                return tryToMoveNorth(instruction);
            case SO:
                return tryToMoveSouth(instruction);
            case EA:
                return tryToMoveEast(instruction);
            case WE:
                return tryToMoveWest(instruction);
            default:
                throw new MiningMachineException("I don't know how, but you managed to provide invalid movement instructions...");
        }
    }

    private boolean tryToMoveWest(MoveInstruction instruction) {
        for (int i = 0; i < instruction.getSteps(); i++) {
            if (currentPosition.getWithAddedX(-1).getX() >= 0) {
                if (!currentField.hasVerticalBlockage(currentPosition.getWithAddedX(-1), Direction.WE)) {
                    moveOneStepHorizontally(instruction.getDirection());
                } else return true;
            } else return true;
        }
        return false;
    }

    private boolean tryToMoveEast(MoveInstruction instruction) {
        for (int i = 0; i < instruction.getSteps(); i++) {
            if (currentPosition.getWithAddedX(1).getX() < currentField.getWidth()) {
                if (!currentField.hasVerticalBlockage(currentPosition.getWithAddedX(1), Direction.EA)) {
                    moveOneStepHorizontally(instruction.getDirection());
                } else return true;
            } else return true;
        }
        return false;
    }

    private boolean tryToMoveSouth(MoveInstruction instruction) {
        for (int i = 0; i < instruction.getSteps(); i++) {
            if (currentPosition.getWithAddedY(-1).getY() >= 0) {
                if (!currentField.hasHorizontalBlockage(currentPosition.getWithAddedY(-1), Direction.SO)) {
                    moveOneStepVertically(instruction.getDirection());
                } else return true;
            } else return true;
        }
        return false;
    }

    private boolean tryToMoveNorth(MoveInstruction instruction) {
        for (int i = 0; i < instruction.getSteps(); i++) {
            if (currentPosition.getWithAddedY(1).getY() < currentField.getHeight()) {
                if (!currentField.hasHorizontalBlockage(currentPosition.getWithAddedY(1), Direction.NO)) {
                    moveOneStepVertically(instruction.getDirection());
                } else return true;
            } else return true;
        }
        return false;
    }

    private void moveOneStepHorizontally(Direction direction) {
        int directionFactor;
        switch (direction) {
            case EA:
                directionFactor = 1;
                break;
            case WE:
                directionFactor = -1;
                break;
            default:
                return;
        }
        this.currentPosition = currentPosition.getWithAddedX(directionFactor);
        currentField.getSquare(currentPosition).setBlocked();
        currentField.getSquare(currentPosition.getWithAddedX(-1 * directionFactor)).setUnblocked();
    }

    private void moveOneStepVertically(Direction direction) {
        int directionFactor;
        switch (direction) {
            case NO:
                directionFactor = 1;
                break;
            case SO:
                directionFactor = -1;
                break;
            default:
                return;
        }
        this.currentPosition = currentPosition.getWithAddedY(directionFactor);
        currentField.getSquare(currentPosition).setBlocked();
        currentField.getSquare(currentPosition.getWithAddedY(-1 * directionFactor)).setUnblocked();
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
                this.currentField.getEntrySquare().setBlocked();
                return true;
            }
        } else return false;
    }
}

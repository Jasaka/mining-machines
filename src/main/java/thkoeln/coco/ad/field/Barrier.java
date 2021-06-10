package thkoeln.coco.ad.field;

import lombok.Getter;
import thkoeln.coco.ad.instruction.BarrierInstruction;
import thkoeln.coco.ad.instruction.InstructionFactory;
import thkoeln.coco.ad.miningMachine.MiningMachineException;
import thkoeln.coco.ad.primitive.Coordinate;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Barrier {
    private Coordinate startCoordinate;
    private Coordinate endCoordinate;

    protected Barrier(){}

    public Barrier(Coordinate startCoordinate, Coordinate endCoordinate) {
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
    }

    public static Barrier fromString(String barrierString) {
        try {
            BarrierInstruction instruction = InstructionFactory.getInstruction(barrierString);
            checkValidBarrierCoordinates(instruction);

            return new Barrier(instruction.getStartCoordinate(), instruction.getEndCoordinate());
        } catch (ClassCastException e) {
            throw new MiningMachineException("Tried to apply wrong command type");
        }
    }

    private static void checkValidBarrierCoordinates(BarrierInstruction instruction) {
        Integer x1 = instruction.getStartCoordinate().getX();
        Integer y1 = instruction.getStartCoordinate().getY();
        Integer x2 = instruction.getEndCoordinate().getX();
        Integer y2 = instruction.getEndCoordinate().getY();
        if (!(x1.equals(x2) || y1.equals(y2))) {
            throw new MiningMachineException("Invalid Barrier String - Tried creating diagonal barrier");
        }
        if ((x1.equals(x2) && y1.equals(y2))) {
            throw new MiningMachineException("Invalid Barrier String - Tried creating dot barrier");
        }
    }
}

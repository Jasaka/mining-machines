package thkoeln.coco.ad.field;

import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;
import thkoeln.coco.ad.instruction.BarrierInstruction;
import thkoeln.coco.ad.instruction.InstructionFactory;
import thkoeln.coco.ad.miningMachine.MiningMachineException;
import thkoeln.coco.ad.primitive.Coordinate;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Barrier {
    /*
    TODO: get coordinates to work
    private Coordinate startCoordinate;
    private Coordinate endCoordinate;
    */
    private String barrierString;

    protected Barrier(){}

    // TODO: Remove temporary String constructor
    public Barrier(String barrierString){
        this.barrierString = barrierString;
    }

    public Barrier(Coordinate startCoordinate, Coordinate endCoordinate) {
        /*
        TODO: get coordinates to work
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        */
    }

    public static Barrier fromString(String barrierString) {
        try {
            BarrierInstruction instruction = InstructionFactory.getInstruction(barrierString);
            checkValidBarrierCoordinates(instruction);

            //return new Barrier(instruction.getStartCoordinate(), instruction.getEndCoordinate());
            return new Barrier(barrierString);
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

    @Transient
    public Coordinate getStartCoordinate(){
        BarrierInstruction instruction = InstructionFactory.getInstruction(this.barrierString);
        return instruction.getStartCoordinate();
    }

    @Transient
    public Coordinate getEndCoordinate(){
        BarrierInstruction instruction = InstructionFactory.getInstruction(this.barrierString);
        return instruction.getEndCoordinate();
    }
}

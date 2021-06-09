package thkoeln.coco.ad.field;

import lombok.Getter;
import thkoeln.coco.ad.instruction.CoordinateInstruction;
import thkoeln.coco.ad.instruction.InstructionFactory;
import thkoeln.coco.ad.miningMachine.MiningMachineException;
import thkoeln.coco.ad.primitive.Coordinate;

import java.util.UUID;

@Getter
public class Connection {

    private final UUID id = UUID.randomUUID();
    Coordinate sourcePoint;
    Coordinate destinationPoint;

    public Connection(String sourcePointString, String destinationPointString) {
        try {
            CoordinateInstruction sourcePointInstruction = InstructionFactory.getInstruction(sourcePointString);
            CoordinateInstruction destinationPointInstruction = InstructionFactory.getInstruction(destinationPointString);

            this.sourcePoint = sourcePointInstruction.getCoordinate();
            this.destinationPoint = destinationPointInstruction.getCoordinate();
        } catch (ClassCastException e) {
            throw new MiningMachineException("Tried to apply wrong command type");
        }

    }
}

package thkoeln.coco.ad.instruction;

import lombok.Getter;
import thkoeln.coco.ad.miningMachine.MiningMachineException;
import thkoeln.coco.ad.primitive.Coordinate;

@Getter
public class BarrierInstruction extends Instruction {

    private final Coordinate startCoordinate;
    private final Coordinate endCoordinate;

    public BarrierInstruction(String inputString) {
        String[] coordinateStrings = inputString.split("-");
        try {
            CoordinateInstruction startCoordinateInstruction = InstructionFactory.getInstruction(coordinateStrings[0]);
            CoordinateInstruction endCoordinateInstruction = InstructionFactory.getInstruction(coordinateStrings[1]);

            this.startCoordinate = startCoordinateInstruction.getCoordinate();
            this.endCoordinate = endCoordinateInstruction.getCoordinate();
        } catch (ClassCastException e) {
            throw new MiningMachineException("Tried to apply wrong command type");
        }
    }
}

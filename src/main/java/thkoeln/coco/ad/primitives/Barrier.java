package thkoeln.coco.ad.primitives;

import lombok.Getter;
import lombok.NoArgsConstructor;
import thkoeln.coco.ad.instruction.BarrierInstruction;
import thkoeln.coco.ad.miningMachine.MiningMachineException;
import thkoeln.coco.ad.parser.InputParser;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
public class Barrier {
    private String startSquareString;
    private String endSquareString;

    public Barrier(String startSquareString, String endSquareString) {
        this.startSquareString = startSquareString;
        this.endSquareString = endSquareString;
    }

    public static Barrier createBarrier(BarrierInstruction barrierInstruction){
        Square startSquare = InputParser.parseSquareString(barrierInstruction.getStartSquare());
        Square endSquare = InputParser.parseSquareString(barrierInstruction.getEndSquare());
        if (barrierInstruction.getInstruction().equals("br") && isBarrierValid(startSquare, endSquare)){
            return new Barrier(barrierInstruction.getStartSquare(), barrierInstruction.getEndSquare());
        }
        throw new MiningMachineException("Tried barrier creation with wrong command");
    }

    private static boolean isBarrierValid(Square squareOne, Square squareTwo){
        if (squareOne.getCoordinateX().equals(squareTwo.getCoordinateX()) || squareOne.getCoordinateY().equals(squareTwo.getCoordinateY()) ){
            return true;
        }
        throw new MiningMachineException("Diagonal barrier attempt");
    }
}

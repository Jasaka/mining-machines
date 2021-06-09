package thkoeln.coco.ad.instruction;

import lombok.Getter;
import thkoeln.coco.ad.primitive.Coordinate;

@Getter
public class CoordinateInstruction extends Instruction{
    private final Coordinate coordinate;

    public CoordinateInstruction(String inputString) {
        String[] coordinateStrings = inputString.replaceAll("[\\)\\(]","").split(",");

        Integer coordinateX = Integer.parseInt(coordinateStrings[0]);
        Integer coordinateY = Integer.parseInt(coordinateStrings[1]);

        this.coordinate = new Coordinate(coordinateX, coordinateY);
    }
}

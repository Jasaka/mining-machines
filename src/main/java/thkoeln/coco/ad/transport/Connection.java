package thkoeln.coco.ad.transport;

import lombok.Getter;
import thkoeln.coco.ad.field.Field;
import thkoeln.coco.ad.instruction.CoordinateInstruction;
import thkoeln.coco.ad.instruction.InstructionFactory;
import thkoeln.coco.ad.miningMachine.MiningMachineException;
import thkoeln.coco.ad.primitive.Coordinate;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Connection {

    @Id
    @Getter
    private final UUID id = UUID.randomUUID();

    @Getter
    @OneToOne
    private Field sourceField;

    @Getter
    @OneToOne
    private Field destinationField;

    @ElementCollection
    private final List<Coordinate> coordinates = new ArrayList<>();

    protected Connection(){}

    public Connection(Field sourceField, String sourcePointString, Field destinationField, String destinationPointString) {
        try {
            CoordinateInstruction sourcePointInstruction = InstructionFactory.getInstruction(sourcePointString);
            CoordinateInstruction destinationPointInstruction = InstructionFactory.getInstruction(destinationPointString);

            this.sourceField = sourceField;
            this.destinationField = destinationField;

            this.coordinates.add(sourcePointInstruction.getCoordinate());
            this.coordinates.add(destinationPointInstruction.getCoordinate());

            try {
                sourceField.getSquare(sourcePointInstruction.getCoordinate());
                destinationField.getSquare(destinationPointInstruction.getCoordinate());
            } catch (IndexOutOfBoundsException e){
                throw new MiningMachineException("Tried to connect nonexistent coordinates");
            }

        } catch (ClassCastException e) {
            throw new MiningMachineException("Tried to apply wrong command type");
        }
    }

    public Coordinate getSourceCoordinate(){
        return coordinates.get(0);
    }

    public Coordinate getDestinationCoordinate(){
        return coordinates.get(1);
    }
}

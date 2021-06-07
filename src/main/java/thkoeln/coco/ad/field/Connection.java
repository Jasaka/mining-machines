package thkoeln.coco.ad.field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import thkoeln.coco.ad.parser.InputParser;
import thkoeln.coco.ad.primitives.Square;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Connection {
    @Id
    private final UUID id = UUID.randomUUID();
    private UUID transportTechnologyId = null;
    private UUID sourceFieldId = null;
    private UUID destinationFieldId = null;

    @ElementCollection(targetClass = Square.class, fetch = FetchType.EAGER)
    private final List<Square> connectedSquares = new ArrayList<>();

    public Connection(UUID transportTechnologyId, UUID sourceFieldId, Square sourceSquare, UUID destinationFieldId, Square destinationSquare) {
        this.transportTechnologyId = transportTechnologyId;
        this.sourceFieldId = sourceFieldId;
        this.connectedSquares.add(sourceSquare);
        this.destinationFieldId = destinationFieldId;
        this.connectedSquares.add(destinationSquare);
    }

    public static Connection createConnection(UUID transportTechnologyId, UUID sourceFieldId, String sourcePointString, UUID destinationFieldId, String destinationPointString) {
        InputParser parser = new InputParser();
        Square sourceSquare = parser.parseSquareString(sourcePointString);
        Square destinationSquare = parser.parseSquareString(destinationPointString);
        return new Connection(transportTechnologyId, sourceFieldId, sourceSquare, destinationFieldId, destinationSquare);
    }
}

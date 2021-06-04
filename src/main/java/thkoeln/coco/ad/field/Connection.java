package thkoeln.coco.ad.field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import thkoeln.coco.ad.parser.InputParser;
import thkoeln.coco.ad.primitives.Node;

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

    @ElementCollection(targetClass = Node.class, fetch = FetchType.EAGER)
    private final List<Node> connectedNodes = new ArrayList<>();

    public Connection(UUID transportTechnologyId, UUID sourceFieldId, Node sourceNode, UUID destinationFieldId, Node destinationNode) {
        this.transportTechnologyId = transportTechnologyId;
        this.sourceFieldId = sourceFieldId;
        this.connectedNodes.add(sourceNode);
        this.destinationFieldId = destinationFieldId;
        this.connectedNodes.add(destinationNode);
    }

    public static Connection createConnection(UUID transportTechnologyId, UUID sourceFieldId, String sourcePointString, UUID destinationFieldId, String destinationPointString) {
        InputParser parser = new InputParser();
        Node sourceNode= parser.parseNodeString(sourcePointString);
        Node destinationNode= parser.parseNodeString(destinationPointString);
        return new Connection(transportTechnologyId, sourceFieldId, sourceNode, destinationFieldId, destinationNode);
    }
}

package thkoeln.coco.ad.field;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Connection {
    @Id
    private final UUID id = UUID.randomUUID();
    private UUID transportTechnologyId;
    private UUID sourceFieldId;
    private String sourcePointString;
    private UUID destinationFieldId;
    private String destinationPointString;


    public Connection(UUID transportTechnologyId, UUID sourceFieldId, String sourcePointString, UUID destinationFieldId, String destinationPointString) {
        this.transportTechnologyId = transportTechnologyId;
        this.sourceFieldId = sourceFieldId;
        this.sourcePointString = sourcePointString;
        this.destinationFieldId = destinationFieldId;
        this.destinationPointString = destinationPointString;
    }

    public static Connection createConnection(UUID transportTechnologyId, UUID sourceFieldId, String sourcePointString, UUID destinationFieldId, String destinationPointString) {
        return new Connection(transportTechnologyId, sourceFieldId, sourcePointString, destinationFieldId, destinationPointString);
    }
}

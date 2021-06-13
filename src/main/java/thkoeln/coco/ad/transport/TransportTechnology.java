package thkoeln.coco.ad.transport;

import lombok.Getter;
import thkoeln.coco.ad.field.Field;
import thkoeln.coco.ad.primitive.Coordinate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class TransportTechnology {

    @Id
    private final UUID id = UUID.randomUUID();
    private String technologyName;

    @OneToMany
    private final List<Connection> connections = new ArrayList<>();

    protected TransportTechnology() {
    }

    public TransportTechnology(String technology) {
        this.technologyName = technology;
    }

    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public boolean hasExistingConnection(Field sourceField, Coordinate sourceCoordinate, Field destinationField) {
        boolean hasConnection = false;
        for (Connection connection : connections) {
            if (connection.getSourceField().getId() == sourceField.getId() && connection.getDestinationField().getId() == destinationField.getId()) {
                hasConnection = connection.getSourceCoordinate().equalsCoordinate(sourceCoordinate);
            } else if (connection.getDestinationField().getId() == sourceField.getId() && connection.getSourceField().getId() == destinationField.getId()) {
                hasConnection = connection.getDestinationCoordinate().equalsCoordinate(sourceCoordinate);
            }
            if (hasConnection) break;
        }
        return hasConnection;
    }

    public Coordinate getDestinationCoordinate(Field sourceField, Coordinate sourceCoordinate, Field destinationField) {
        for (Connection connection : connections) {
            if (connection.getSourceField().getId() == sourceField.getId() && connection.getDestinationField().getId() == destinationField.getId()) {
                if(connection.getSourceCoordinate().equalsCoordinate(sourceCoordinate)){
                    return connection.getDestinationCoordinate();
                }
            } else if (connection.getDestinationField().getId() == sourceField.getId() && connection.getSourceField().getId() == destinationField.getId()) {
                if(connection.getDestinationCoordinate().equalsCoordinate(sourceCoordinate)){
                    return connection.getSourceCoordinate();
                }
            }
        }
        return null;
    }
}

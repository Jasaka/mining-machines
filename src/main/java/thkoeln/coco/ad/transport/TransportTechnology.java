package thkoeln.coco.ad.transport;

import lombok.Getter;

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

    //  TODO: fix german primary key violation exception
    /*@OneToMany
    private final List<Connection> connections = new ArrayList<>();*/
    @ElementCollection(fetch = FetchType.EAGER)
    private final List<UUID> connectionIds = new ArrayList<>();

    protected TransportTechnology() {
    }

    public TransportTechnology(String technology) {
        this.technologyName = technology;
    }

    public void addConnection(Connection connection) {
//        connections.add(connection);
        connectionIds.add(connection.getId());
    }
}

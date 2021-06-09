package thkoeln.coco.ad.transport;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    protected TransportTechnology(){}
    public TransportTechnology(String technology) {
        this.technologyName = technology;
    }
}

package thkoeln.coco.ad.field;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
public class TransportTechnology {

    @Id
    private final UUID id = UUID.randomUUID();
    private String technologyName;

    protected TransportTechnology(){}
    public TransportTechnology(String technology) {
        this.technologyName = technology;
    }
}

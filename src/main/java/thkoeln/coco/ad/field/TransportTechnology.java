package thkoeln.coco.ad.field;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class TransportTechnology {

    @Id
    @Getter
    private final UUID id = UUID.randomUUID();

    @Getter
    private String technology = null;

    private TransportTechnology(String technology){
        this.technology = technology;
    }

    public static TransportTechnology createTechnology(String technology){
        return new TransportTechnology(technology);
    }
}

package thkoeln.coco.ad.field;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TransportTechnology {

    private final UUID id = UUID.randomUUID();
    private final String technologyName;

    public TransportTechnology(String technology) {
        this.technologyName = technology;
    }
}

package thkoeln.coco.ad.field;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Field {
    private final UUID id = UUID.randomUUID();
    private final Integer height, width;

    public Field(Integer height, Integer width) {
        this.height = height;
        this.width = width;
    }
}

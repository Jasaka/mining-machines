package thkoeln.coco.ad.field;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
public class Field {
    @Id
    private final UUID id = UUID.randomUUID();

    private Integer height, width;

    protected Field(){}

    public Field(Integer height, Integer width) {
        this.height = height;
        this.width = width;
    }
}

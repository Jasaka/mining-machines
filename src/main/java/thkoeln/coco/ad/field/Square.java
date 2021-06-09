package thkoeln.coco.ad.field;

import lombok.Getter;
import thkoeln.coco.ad.primitive.Coordinate;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Entity
public class Square {

    @Id
    private final UUID id = UUID.randomUUID();

    @Embedded
    private Coordinate coordinate;

    private Boolean blocked = false;

    protected Square(){}

    public Square(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setBlocked(){
        this.blocked = true;
    }

    public void setUnblocked(){
        this.blocked = false;
    }
}

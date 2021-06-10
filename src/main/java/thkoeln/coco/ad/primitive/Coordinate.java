package thkoeln.coco.ad.primitive;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter(AccessLevel.PROTECTED)
public class Coordinate {
    private Integer x;
    private Integer y;

    protected Coordinate(){}
    public Coordinate(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
}

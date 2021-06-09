package thkoeln.coco.ad.primitive;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Coordinate {
    private Integer x;
    private Integer y;

    protected Coordinate(){}
    public Coordinate(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
}

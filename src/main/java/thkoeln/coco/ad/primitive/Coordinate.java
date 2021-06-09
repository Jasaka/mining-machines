package thkoeln.coco.ad.primitive;

import lombok.Getter;

@Getter
public class Coordinate {
    private final Integer x;
    private final Integer y;

    public Coordinate(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
}

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

    protected Coordinate() {
    }

    public Coordinate(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }


    public boolean equalsCoordinate(Coordinate coordinate){
        return this.x.equals(coordinate.getX()) && this.y.equals(coordinate.getY());
    }

    public Coordinate getWithAddedX(Integer addition){
        return new Coordinate(this.x + addition, this.y);
    }

    public Coordinate getWithAddedY(Integer addition){
        return new Coordinate(this.x, this.y + addition);
    }
}

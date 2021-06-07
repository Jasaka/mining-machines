package thkoeln.coco.ad.primitives;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
public class Square {
    private Integer coordinateX = null;
    private Integer coordinateY = null;
    private boolean isBlocked = false;

    private Square(Integer coordinateX, Integer coordinateY){
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public static Square createSquareFromString(String coordinateString){
        coordinateString = coordinateString.replaceAll("[)(]", "");
        String[] coordinate = coordinateString.split(",");

        Integer coordinateX = Integer.parseInt(coordinate[0]);
        Integer coordinateY = Integer.parseInt(coordinate[1]);

        return new Square(coordinateX, coordinateY);
    }

    public static Square createSquare(int x, int y) {
        Integer coordinateX = x;
        Integer coordinateY = y;

        return new Square(coordinateX, coordinateY);
    }

    public void toggleBlockage(){
        this.isBlocked = !this.isBlocked;
    }
}

package thkoeln.coco.ad.field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import thkoeln.coco.ad.primitives.Barrier;
import thkoeln.coco.ad.primitives.Square;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Transactional
@NoArgsConstructor
public class Field {
    @Id
    private final UUID id = UUID.randomUUID();

    @ElementCollection(targetClass = Square.class, fetch = FetchType.LAZY)
    private final List<Square> squares = new ArrayList<>();

    @ElementCollection(targetClass = Barrier.class, fetch = FetchType.LAZY)
    private final List<Barrier> barriers = new ArrayList<>();

    private Field(Integer height, Integer width) {
        generateSquares(height, width);
    }

    private void generateSquares(Integer height, Integer width) {
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                squares.add(Square.createSquare(x,y));
            }
        }
    }


    public static Field initializeFromDimensions(Integer height, Integer width) {
        if (isValidDimension(height, width)){
            return new Field(height, width);
        } else throw new IllegalArgumentException("Wrong Dimensions");
    }

    public static boolean isValidDimension(Integer height, Integer width) {
        return width >= 0 && height >= 0;
    }

    public void addBarrier(Barrier barrier){
        barriers.add(barrier);
    }
}

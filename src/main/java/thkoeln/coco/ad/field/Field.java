package thkoeln.coco.ad.field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import thkoeln.coco.ad.primitives.Barrier;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Field {
    @Id
    private final UUID id = UUID.randomUUID();

    private Integer height = null;
    private Integer width = null;

    @ElementCollection(targetClass = Barrier.class, fetch = FetchType.EAGER)
    private final List<Barrier> barriers = new ArrayList<>();

    private Field(Integer height, Integer width) {
        this.height = height;
        this.width = width;
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

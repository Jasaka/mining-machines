package thkoeln.coco.ad.field;

import lombok.Getter;
import thkoeln.coco.ad.primitives.Barrier;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Field {
    @Id
    @Getter
    private final UUID id = UUID.randomUUID();

    @Getter
    private final Integer height;
    @Getter
    private final Integer width;

    @Getter
    @ElementCollection(targetClass = Barrier.class, fetch = FetchType.EAGER)
    private final List<Barrier> barriers = new ArrayList<>();

    private Field(Integer height, Integer width) {
        this.height = height;
        this.width = width;
    }

    protected Field() {
        this.height = 5;
        this.width = 5;
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

package thkoeln.coco.ad.field;

import lombok.Getter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
public class Field {
    @Id
    private final UUID id = UUID.randomUUID();

    private Integer height, width;

    //TODO: Manage Cartesian product/EAGER-LAZY Problems
    @OneToMany(targetEntity = Square.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final List<Square> squares = new ArrayList<>();

    @Transient
    private Square[][] field;

    //TODO: Manage Cartesian product/EAGER-LAZY Problems
    @ElementCollection(targetClass = Barrier.class, fetch = FetchType.EAGER)
    private final Set<Barrier> barriers = new HashSet<>();

    protected Field(){}

    public Field(Integer height, Integer width) {
        this.height = height;
        this.width = width;
    }

    public void addBarrier(Barrier barrier) {
        barriers.add(barrier);
    }

    public void removeBarrier(Barrier barrier) {
        barriers.remove(barrier);
    }
}

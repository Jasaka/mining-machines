package thkoeln.coco.ad.field;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class Field {
    @Id
    private final UUID id = UUID.randomUUID();

    private Integer height, width;

    //TODO: Manage Cartesian product/EAGER-LAZY Problems
    @OneToMany(targetEntity = Square.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Square> squares = new ArrayList<>();

    @Transient
    private Square[][] field;

    //TODO: Manage Cartesian product/EAGER-LAZY Problems
    @ElementCollection(targetClass = Barrier.class, fetch = FetchType.EAGER)
    private final List<Barrier> barriers = new ArrayList<>();

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

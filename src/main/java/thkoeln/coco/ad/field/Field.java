package thkoeln.coco.ad.field;

import lombok.Getter;
import thkoeln.coco.ad.miningMachine.MiningMachineException;

import javax.persistence.*;
import java.util.*;

@Entity
public class Field {
    @Getter
    @Id
    private final UUID id = UUID.randomUUID();

    @Getter
    private Integer height, width;

    @Getter
    @OneToMany(targetEntity = Square.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final List<Square> squares = new ArrayList<>();

    @Transient
    private Square[][] field;

    @Getter
    @ElementCollection(targetClass = Barrier.class, fetch = FetchType.EAGER)
    private final Set<Barrier> barriers = new HashSet<>();

    protected Field(){}

    public Field(Integer height, Integer width) {
        this.height = height;
        this.width = width;
        initField();
    }

    private void initField(){
        field = new Square[this.width][this.height];
        for (int x = 0; x < this.width; x++){
            for (int y = 0; y < this.height; y++){
                field[x][y] = Square.fromInt(x, y);
                squares.add(field[x][y]);
            }
        }
    }

    private void ensureFieldIsInitialized(){
        if (field == null || field.length == 0){
            field = new Square[this.width][this.height];
            for (Square square : squares) {
                field[square.getCoordinate().getX()][square.getCoordinate().getY()] = square;
            }
        }
    }

    public void addBarrier(Barrier barrier) {
        barriers.add(barrier);
    }

    public void removeBarrier(Barrier barrier) {
        barriers.remove(barrier);
    }

    public Square getEntrySquare(){
        ensureFieldIsInitialized();
        return field[0][0];
    }

    public Square getSquare(int x, int y){
        ensureFieldIsInitialized();
        try {
            return field[x][y];
        } catch (NullPointerException e){
            throw new MiningMachineException("Tried Accessing nonexistent square.");
        }
    }
}

package thkoeln.coco.ad.field;

import lombok.Getter;
import thkoeln.coco.ad.miningMachine.MiningMachineException;
import thkoeln.coco.ad.primitive.Coordinate;
import thkoeln.coco.ad.primitive.Direction;

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
    @OneToMany
    private final List<Square> squares = new ArrayList<>();

    @Transient
    private Square[][] field;

    @Getter
    @ElementCollection(targetClass = Barrier.class)
    private final List<Barrier> barriers = new ArrayList<>();

    protected Field() {
    }

    public Field(Integer height, Integer width) {
        this.height = height;
        this.width = width;
        initField();
    }

    private void initField() {
        field = new Square[this.width][this.height];
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                field[x][y] = Square.fromInt(x, y);
                squares.add(field[x][y]);
            }
        }
        barriers.add(new Barrier(new Coordinate(0,0), new Coordinate(0,height)));
        barriers.add(new Barrier(new Coordinate(0,0), new Coordinate(width,0)));
        barriers.add(new Barrier(new Coordinate(width,0), new Coordinate(width,height)));
        barriers.add(new Barrier(new Coordinate(0,height), new Coordinate(width,height)));
    }

    private void ensureFieldIsInitialized() {
        if (field == null || field.length == 0) {
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

    public Square getEntrySquare() {
        ensureFieldIsInitialized();
        return field[0][0];
    }

    public Square getSquare(Coordinate coordinate) {
        ensureFieldIsInitialized();
        try {
            return field[coordinate.getX()][coordinate.getY()];
        } catch (NullPointerException e) {
            throw new MiningMachineException("Tried Accessing nonexistent square.");
        }
    }

    public boolean hasHorizontalBlockage(Coordinate targetSquare, Direction direction) {
        switch (direction) {
            case NO:
                if (this.getSquare(targetSquare).getBlockedByMachine()){
                    return true;
                }
                return hasHorizontalBarrierBlockage(targetSquare);
            case SO:
                if (this.getSquare(targetSquare).getBlockedByMachine()){
                    return true;
                }
                return hasHorizontalBarrierBlockage(targetSquare.getWithAddedY(-1));
        }
        return true;
    }

    public boolean hasVerticalBlockage(Coordinate targetSquare, Direction direction) {
        switch (direction) {
            case EA:
                if (this.getSquare(targetSquare).getBlockedByMachine()){
                    return true;
                }
                return hasVerticalBarrierBlockage(targetSquare);
            case WE:
                if (this.getSquare(targetSquare).getBlockedByMachine()){
                    return true;
                }
                return hasVerticalBarrierBlockage(targetSquare.getWithAddedX(1));
        }
        return true;
    }

    private boolean hasVerticalBarrierBlockage(Coordinate targetSquare){
        boolean targetSquareBlocked = false;
        boolean aboveTargetSquareBlocked = false;
        for (Barrier barrier: barriers) {
            for (Coordinate coordinate: barrier.getEncompassingCoordinates()) {
                if (targetSquare.equals(coordinate)){
                    targetSquareBlocked = true;
                }
                if (targetSquare.getWithAddedY(1).equals(coordinate)){
                    aboveTargetSquareBlocked = true;
                }
            }
        }
        return targetSquareBlocked && aboveTargetSquareBlocked;
    }

    private boolean hasHorizontalBarrierBlockage(Coordinate targetSquare){
        boolean targetSquareBlocked = false;
        boolean rightOfTargetSquareBlocked = false;
        for (Barrier barrier: barriers) {
            for (Coordinate coordinate: barrier.getEncompassingCoordinates()) {
                if (targetSquare.equals(coordinate)){
                    targetSquareBlocked = true;
                }
                if (targetSquare.getWithAddedX(1).equals(coordinate)){
                    rightOfTargetSquareBlocked = true;
                }
            }
        }
        return targetSquareBlocked && rightOfTargetSquareBlocked;
    }
}

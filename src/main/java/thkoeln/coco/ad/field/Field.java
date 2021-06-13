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
    @Column(columnDefinition = "BINARY(16)")
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
        if (this.getSquare(targetSquare).getBlockedByMachine()) {
            System.out.println(targetSquare.toString() + " is Blocked by Mining Machine");
            return true;
        } else switch (direction) {
            case NO:
                return hasHorizontalBarrierBlockage(targetSquare);
            case SO:
                return hasHorizontalBarrierBlockage(targetSquare.getWithAddedY(-1));
            default:
                return false;
        }
    }

    public boolean hasVerticalBlockage(Coordinate targetSquare, Direction direction) {
        if (this.getSquare(targetSquare).getBlockedByMachine()) {
            System.out.println(targetSquare.toString() + " is Blocked by Mining Machine");
            return true;
        } else switch (direction) {
            case EA:
                return hasVerticalBarrierBlockage(targetSquare);
            case WE:
                return hasVerticalBarrierBlockage(targetSquare.getWithAddedX(1));
            default:
                return false;
        }
    }

    private boolean hasVerticalBarrierBlockage(Coordinate targetSquare) {
        boolean hasBarrier = false;
        for (Barrier barrier : barriers) {
            boolean targetSquareBlocked = false;
            boolean aboveTargetSquareBlocked = false;
            for (Coordinate coordinate : barrier.getEncompassingCoordinates()) {
                if (targetSquare.equals(coordinate)) {
                    targetSquareBlocked = true;
                }
                if (targetSquare.getWithAddedY(1).equals(coordinate)) {
                    aboveTargetSquareBlocked = true;
                }
            }
            hasBarrier = targetSquareBlocked && aboveTargetSquareBlocked;
            if (hasBarrier) break;
        }
        return hasBarrier;
    }

    private boolean hasHorizontalBarrierBlockage(Coordinate targetSquare) {
        boolean hasBarrier = false;
        for (Barrier barrier : barriers) {
            boolean targetSquareBlocked = false;
            boolean rightOfTargetSquareBlocked = false;
            for (Coordinate coordinate : barrier.getEncompassingCoordinates()) {
                if (targetSquare.equalsCoordinate(coordinate)) {
                    targetSquareBlocked = true;
                }
                if (targetSquare.getWithAddedX(1).equalsCoordinate(coordinate)) {
                    rightOfTargetSquareBlocked = true;
                }
            }
            hasBarrier = targetSquareBlocked && rightOfTargetSquareBlocked;
            if (hasBarrier) break;
        }
        return hasBarrier;
    }
}

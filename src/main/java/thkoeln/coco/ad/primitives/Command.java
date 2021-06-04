package thkoeln.coco.ad.primitives;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Command {
    private UUID targetedId = null;
    private final String command;
    private Integer steps = null;
    private Node nodeOne = null;
    private Node nodeTwo = null;

    private Command(String command, Integer steps) {
        this.command = command;
        this.steps = steps;
    }

    private Command(String command, UUID targetedId) {
        this.command = command;
        this.targetedId = targetedId;
    }

    public Command(Node nodeOne, Node nodeTwo) {
        this.command = "br";
        this.nodeOne = nodeOne;
        this.nodeTwo = nodeTwo;
    }

    public static Command generateMoveCommand(String command, Integer steps) {
        return new Command(command, steps);
    }

    public static Command generateTransportCommand(UUID targetedId) {
        return new Command("tr", targetedId);
    }

    public static Command generateEntryCommand(UUID targetedId) {
        return new Command("en", targetedId);
    }

    public static Command generateBarrierCommand(Node nodeOne, Node nodeTwo) {
        return new Command(nodeOne, nodeTwo);
    }
}

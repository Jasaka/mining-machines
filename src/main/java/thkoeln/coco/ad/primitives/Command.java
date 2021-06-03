package thkoeln.coco.ad.primitives;

import java.util.UUID;

public class Command {
    UUID targetedId = null;
    String command = null;
    Integer steps = null;

    private Command(String command, Integer steps) {
        this.command = command;
        this.steps = steps;
    }

    private Command(String command, UUID targetedId) {
        this.command = command;
        this.targetedId = targetedId;
    }

    public static Command generateMoveCommand(String command, Integer steps){
        return new Command(command, steps);
    }

    public static Command generateTransportCommand(UUID targetedId){
        return new Command("tr", targetedId);
    }

    public static Command generateEntryCommand(UUID targetedId){
        return new Command("en", targetedId);
    }

}

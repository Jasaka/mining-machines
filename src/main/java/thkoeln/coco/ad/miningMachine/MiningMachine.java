package thkoeln.coco.ad.miningMachine;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class MiningMachine {

    @Getter
    private UUID id = UUID.randomUUID();

    @Getter
    @Setter
    private String name;

    public MiningMachine( String name ) {
        this.name = name;
    }
}

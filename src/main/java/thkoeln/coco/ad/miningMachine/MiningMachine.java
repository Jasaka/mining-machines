package thkoeln.coco.ad.miningMachine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import thkoeln.coco.ad.CentralControlService;
import thkoeln.coco.ad.instruction.Instruction;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@NoArgsConstructor
@Entity
public class MiningMachine {

    @Id
    @Getter
    private final UUID id = UUID.randomUUID();

    @Getter
    @Setter
    private String name;

    public MiningMachine( String name ) {
        this.name = name;
    }

    public void executeCommand(CentralControlService centralControlService, Instruction instruction) {
    }
}

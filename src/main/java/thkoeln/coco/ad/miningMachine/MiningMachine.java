package thkoeln.coco.ad.miningMachine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@NoArgsConstructor
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
}

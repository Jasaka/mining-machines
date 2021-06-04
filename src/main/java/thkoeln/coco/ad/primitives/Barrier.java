package thkoeln.coco.ad.primitives;

import lombok.Getter;
import lombok.NoArgsConstructor;
import thkoeln.coco.ad.miningMachine.MiningMachineException;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@NoArgsConstructor
@Getter
public class Barrier {

    @ElementCollection(targetClass = Node.class, fetch = FetchType.EAGER)
    private final List<Node> nodes = new ArrayList<>();

    public Barrier(Node nodeOne, Node nodeTwo) {
        this.nodes.add(nodeOne);
        this.nodes.add(nodeTwo);
    }

    public static Barrier createBarrier(Command barrierCommand){
        if (barrierCommand.getCommand().equals("br") && isBarrierValid(barrierCommand.getNodeOne(), barrierCommand.getNodeTwo())){
            return new Barrier(barrierCommand.getNodeOne(), barrierCommand.getNodeTwo());
        }
        throw new MiningMachineException("Tried barrier creation with wrong command");
    }

    private static boolean isBarrierValid(Node nodeOne, Node nodeTwo){
        if (nodeOne.getNodeX().equals(nodeTwo.getNodeX()) || nodeOne.getNodeY().equals(nodeTwo.getNodeY()) ){
            return true;
        }
        throw new MiningMachineException("Diagonal barrier attempt");
    }
}

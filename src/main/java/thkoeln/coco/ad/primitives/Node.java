package thkoeln.coco.ad.primitives;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
public class Node {
    private Integer nodeX = null;
    private Integer nodeY = null;

    private Node(Integer nodeX, Integer nodeY){
        this.nodeX = nodeX;
        this.nodeY = nodeY;
    }

    public static Node createNodeFromString(String nodeString){
        nodeString = nodeString.replaceAll("[)(]", "");
        String[] node = nodeString.split(",");

        Integer nodeX = Integer.parseInt(node[0]);
        Integer nodeY = Integer.parseInt(node[1]);

        return new Node(nodeX, nodeY);
    }
}

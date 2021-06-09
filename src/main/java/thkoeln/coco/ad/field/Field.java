package thkoeln.coco.ad.field;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class Field {
    @Id
    private final UUID id = UUID.randomUUID();

    private Integer height, width;

    @OneToMany
    private final List<Square> squares = new ArrayList<>();

    @Transient
    private Square[][] field;

    protected Field(){}

    public Field(Integer height, Integer width) {
        this.height = height;
        this.width = width;
    }
}

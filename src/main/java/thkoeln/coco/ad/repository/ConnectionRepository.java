package thkoeln.coco.ad.repository;

import org.springframework.data.repository.CrudRepository;
import thkoeln.coco.ad.field.Connection;

import java.util.UUID;

public interface ConnectionRepository extends CrudRepository<Connection, UUID> {
}

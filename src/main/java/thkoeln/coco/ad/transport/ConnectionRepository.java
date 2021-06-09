package thkoeln.coco.ad.transport;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ConnectionRepository extends CrudRepository<Connection, UUID> {
}

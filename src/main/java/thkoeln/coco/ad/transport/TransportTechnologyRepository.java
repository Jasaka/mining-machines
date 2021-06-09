package thkoeln.coco.ad.transport;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TransportTechnologyRepository extends CrudRepository<TransportTechnology, UUID> {
}

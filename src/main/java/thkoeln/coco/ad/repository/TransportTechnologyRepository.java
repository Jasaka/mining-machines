package thkoeln.coco.ad.repository;

import org.springframework.data.repository.CrudRepository;
import thkoeln.coco.ad.field.TransportTechnology;

import java.util.UUID;

public interface TransportTechnologyRepository extends CrudRepository<TransportTechnology, UUID> {
}

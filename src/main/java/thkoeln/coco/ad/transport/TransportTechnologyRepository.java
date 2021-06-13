package thkoeln.coco.ad.transport;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TransportTechnologyRepository extends CrudRepository<TransportTechnology, UUID> {

    @Override
    List<TransportTechnology> findAll();

}

package thkoeln.coco.ad.repository;

import org.springframework.data.repository.CrudRepository;
import thkoeln.coco.ad.field.Field;

import java.util.UUID;

public interface FieldRepository extends CrudRepository<Field, UUID> {
}

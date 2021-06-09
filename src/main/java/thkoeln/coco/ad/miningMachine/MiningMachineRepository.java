package thkoeln.coco.ad.miningMachine;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MiningMachineRepository extends CrudRepository<MiningMachine, UUID> {
}

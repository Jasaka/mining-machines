package thkoeln.coco.ad.repository;

import org.springframework.data.repository.CrudRepository;
import thkoeln.coco.ad.miningMachine.MiningMachine;

import java.util.UUID;

public interface MiningMachineRepository extends CrudRepository<MiningMachine, UUID> {
}

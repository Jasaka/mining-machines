package thkoeln.coco.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thkoeln.coco.ad.field.Field;
import thkoeln.coco.ad.miningMachine.MiningMachine;
import thkoeln.coco.ad.miningMachine.MiningMachineException;

import java.util.UUID;

/**
 * Not the best design, really - you should have one controller per Aggregate.
 * This one is just provided as "one big fat god-like controller" for convenience reasons.
 * In Microservice Architectures (3rd semester), we will introduce a more elegant solution :-).
 */
@RestController
public class CentralRESTController {

    private final CentralControlService centralControlService;

    // todo: add the necessery repositories as members

    /**
     * todo: Add the repos as parameters. Spring will autowire (= auto-instantiate) them.
     */
    @Autowired
    public CentralRESTController(CentralControlService centralControlService) {
        this.centralControlService = centralControlService;
    }


    /**
     * (2b.2) Create a new mining machine
     * todo: set the PostMapping
     */
    @PostMapping("/miningMachines")
    public ResponseEntity<MiningMachine> createNewMiningMachine(@RequestBody MiningMachine miningMachine) {
        try {
            // todo: save the entity using the repo. Create the field beforehand, if it doesn't exist yet

            Field field = centralControlService.createOrGetInitialField();

            centralControlService.addMiningMachine(miningMachine);

            System.out.println(field.getId());
            if (centralControlService.executeCommand(miningMachine.getId(), "[en," + field.getId() + "]")) {
                return new ResponseEntity<>(miningMachine, HttpStatus.CREATED);
            } else return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    /**
     * (2b.3) Get a specific mining machine by ID
     */
    @GetMapping("/miningMachines/{id}")
    public ResponseEntity<MiningMachine> getMiningMachine(@PathVariable UUID id) {
        try {
            MiningMachine machine = centralControlService.getMiningMachineById(id);
            return new ResponseEntity<>(machine, HttpStatus.OK);
        } catch (MiningMachineException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Give a specific mining machine a task
     * todo: set the PutMapping
     */
    @PutMapping("/miningMachines/{id}/tasks/{taskString}")
    public ResponseEntity executeTaskOnMiningMachine(@PathVariable UUID id, @PathVariable String taskString) {
        try {
            centralControlService.executeCommand(id, taskString);
            return new ResponseEntity(HttpStatus.OK);
        } catch (MiningMachineException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

}
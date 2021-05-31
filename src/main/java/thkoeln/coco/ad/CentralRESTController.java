package thkoeln.coco.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thkoeln.coco.ad.miningMachine.MiningMachine;

import java.util.Optional;
import java.util.UUID;

/**
 * Not the best design, really - you should have one controller per Aggregate.
 * This one is just provided as "one big fat god-like controller" for convenience reasons.
 * In Microservice Architectures (3rd semester), we will introduce a more elegant solution :-).
 */
@RestController
public class CentralRESTController {

    // todo: add the necessery repositories as members

    /**
     * todo: Add the repos as parameters. Spring will autowire (= auto-instantiate) them.
     */
    @Autowired
    public CentralRESTController() {
    }


   /**
     * (2b.2) Create a new mining machine
     * todo: set the PostMapping
     */
    @PostMapping( )
    public ResponseEntity<MiningMachine> createNewMiningMachine(@RequestBody MiningMachine miningMachine ) {
        try {
            // todo: save the entity using the repo. Create the field beforehand, if it doesn't exist yet
            MiningMachine newlyCreatedMiningMachine = null;

            return new ResponseEntity<>(newlyCreatedMiningMachine, HttpStatus.CREATED);
        } catch ( Exception e ) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    /**
     * (2b.3) Get a specific mining machine by ID
     * todo: set the GetMapping
     */
    @GetMapping( )
    public ResponseEntity<MiningMachine> getMiningMachine(@PathVariable UUID id) {
        // todo: get the mining machine from the repo and return it
        throw new UnsupportedOperationException();
    }

    /**
     * Give a specific mining machine a task
     * todo: set the PutMapping
     */
    @PutMapping( )
    public ResponseEntity executeTaskOnMiningMachine(@PathVariable UUID id, @PathVariable String taskString) {

        // todo: run the task on the mining machine. Check what can go wrong, and return the proper HttpStatus instead then.
        return new ResponseEntity( HttpStatus.OK );
    }

}
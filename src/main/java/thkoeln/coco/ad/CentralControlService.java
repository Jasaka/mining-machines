package thkoeln.coco.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.coco.ad.field.Barrier;
import thkoeln.coco.ad.field.FieldRepository;
import thkoeln.coco.ad.field.SquareRepository;
import thkoeln.coco.ad.instruction.*;
import thkoeln.coco.ad.miningMachine.MiningMachineException;
import thkoeln.coco.ad.miningMachine.MiningMachineRepository;
import thkoeln.coco.ad.transport.Connection;
import thkoeln.coco.ad.field.Field;
import thkoeln.coco.ad.transport.ConnectionRepository;
import thkoeln.coco.ad.transport.TransportTechnology;
import thkoeln.coco.ad.miningMachine.MiningMachine;
import thkoeln.coco.ad.transport.TransportTechnologyRepository;

import java.util.UUID;

@Service
public class CentralControlService {

    private final FieldRepository fieldRepository;
    private final SquareRepository squareRepository;
    private final ConnectionRepository connectionRepository;
    private final MiningMachineRepository machineRepository;
    private final TransportTechnologyRepository transportRepository;

    @Autowired
    public CentralControlService(FieldRepository fieldRepository, SquareRepository squareRepository, ConnectionRepository connectionRepository, MiningMachineRepository machineRepository, TransportTechnologyRepository transportRepository) {
        this.fieldRepository = fieldRepository;
        this.squareRepository = squareRepository;
        this.connectionRepository = connectionRepository;
        this.machineRepository = machineRepository;
        this.transportRepository = transportRepository;
    }

    /**
     * This method creates a new field.
     *
     * @param height the height of the field
     * @param width  the width of the field
     * @return the UUID of the created field
     */
    public UUID addField(Integer height, Integer width) {
        Field field = new Field(height, width);
        squareRepository.saveAll(field.getSquares());
        fieldRepository.save(field);
        return field.getId();
    }

    /**
     * This method adds a barrier to a given field.
     *
     * @param fieldId       the ID of the field the barrier shall be placed on
     * @param barrierString the end points of the barrier
     */
    public void addBarrier(UUID fieldId, String barrierString) {
        Field field = fieldRepository.findById(fieldId).orElseThrow(() -> new MiningMachineException("Nonexisting FieldID provided: " + fieldId));

        Barrier barrier = Barrier.fromString(barrierString);

        field.addBarrier(barrier);
        squareRepository.saveAll(field.getSquares());
        fieldRepository.save(field);
    }

    /**
     * This method adds a transport technology
     *
     * @param technology the type of the transport technology
     * @return the UUID of the created transport technology
     */
    public UUID addTransportTechnology(String technology) {
        TransportTechnology transportTechnology = new TransportTechnology(technology);
        transportRepository.save(transportTechnology);
        return transportTechnology.getId();
    }

    /**
     * This method adds a traversable connection between two fields based on a transport technology. Connections only work in one direction.
     *
     * @param transportTechnologyId  the transport technology which is used by the connection
     * @param sourceFieldId          the ID of the field where the entry point of the connection is located
     * @param sourcePointString      the point of the entry point
     * @param destinationFieldId     the ID of the field where the exit point of the connection is located
     * @param destinationPointString the point of the exit point
     * @return the UUID of the created connection
     */
    public UUID addConnection(UUID transportTechnologyId, UUID sourceFieldId, String sourcePointString, UUID destinationFieldId, String destinationPointString) {
        TransportTechnology technology = transportRepository.findById(transportTechnologyId).orElseThrow(() -> new MiningMachineException("Nonexisting TransportRepositoryID provided: " + transportTechnologyId));
        Field sourceField = fieldRepository.findById(sourceFieldId).orElseThrow(() -> new MiningMachineException("Nonexisting FieldID provided: " + sourceFieldId));
        Field destinationField = fieldRepository.findById(destinationFieldId).orElseThrow(() -> new MiningMachineException("Nonexisting FieldID provided: " + destinationFieldId));

        System.out.println("   Initializing new Connection...");
        Connection connection = new Connection(sourceField, sourcePointString, destinationField, destinationPointString);
        System.out.println("   - Done");
        System.out.println("   Saving Connection...");
        connectionRepository.save(connection);
        System.out.println("   - Done");

        System.out.println("   Adding connection to technology...");
        technology.addConnection(connection);
        System.out.println("   - Done");
        System.out.println("   Saving technology...");
        transportRepository.save(technology);
        System.out.println("   - Done");
        return connection.getId();
    }

    /**
     * This method adds a new mining machine
     *
     * @param name the name of the mining machine
     * @return the UUID of the created mining machine
     */
    public UUID addMiningMachine(String name) {
        MiningMachine miningMachine = new MiningMachine(name);
        machineRepository.save(miningMachine);
        return miningMachine.getId();
    }

    /**
     * This method lets the mining machine execute a task.
     *
     * @param miningMachineId the ID of the mining machine
     * @param taskString      the given task
     *                        NORTH, WEST, SOUTH, EAST for movement
     *                        TRANSPORT for transport - only works on squares with a connection to another field
     *                        ENTER for setting the initial field where a mining machine is placed. The mining machine will always spawn at (0,0) of the given field.
     * @return true if the command was executed successfully. Else false.
     * (Movement commands are always successful, even if the mining machine hits a barrier or
     * another mining machine, and can move only a part of the steps, or even none at all.
     * tr and en commands are only successful if the action can be performed.)
     */
    public Boolean executeCommand(UUID miningMachineId, String taskString) {
        MiningMachine machine = machineRepository.findById(miningMachineId).orElseThrow(() -> new MiningMachineException("Nonexistent MiningMachineID provided: " + miningMachineId));
        if (InstructionFactory.getInstruction(taskString) instanceof MoveInstruction) {
            MoveInstruction instruction = InstructionFactory.getInstruction(taskString);
            return machine.executeMoveInstruction(instruction);
        }
        if (InstructionFactory.getInstruction(taskString) instanceof TransportInstruction) {
            TransportInstruction instruction = InstructionFactory.getInstruction(taskString);
            Field entryField = fieldRepository.findById(instruction.getTargetedFieldId()).orElseThrow(() -> new MiningMachineException("Nonexistent FieldID provided: " + instruction.getTargetedFieldId()));
            return machine.executeTransportInstruction(instruction);
        }
        if (InstructionFactory.getInstruction(taskString) instanceof EntryInstruction) {
            EntryInstruction instruction = InstructionFactory.getInstruction(taskString);
            Field entryField = fieldRepository.findById(instruction.getTargetedFieldId()).orElseThrow(() -> new MiningMachineException("Nonexistent FieldID provided: " + instruction.getTargetedFieldId()));
            return machine.executeEntryInstruction(entryField);
        }
        throw new MiningMachineException("No executable command provided");
    }

    /**
     * This method returns the field-ID a mining machine is standing on
     *
     * @param miningMachineId the ID of the mining machine
     * @return the UUID of the field the mining machine is located on
     */
    public UUID getMiningMachineFieldId(UUID miningMachineId) {
        MiningMachine machine = machineRepository.findById(miningMachineId).orElseThrow(() -> new MiningMachineException("Nonexisting MiningMachineID provided: " + miningMachineId));
        return machine.getCurrentField().getId();
    }

    /**
     * This method returns the point a mining machine is standing on
     *
     * @param miningMachineId the ID of the mining machine
     * @return the point of the mining machine
     */
    public String getMiningMachinePoint(UUID miningMachineId) {
        MiningMachine machine = machineRepository.findById(miningMachineId).orElseThrow(() -> new MiningMachineException("Nonexisting MiningMachineID provided: " + miningMachineId));
        //TODO
        throw new UnsupportedOperationException();
    }
}

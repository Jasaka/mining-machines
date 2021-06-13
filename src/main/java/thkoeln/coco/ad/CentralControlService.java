package thkoeln.coco.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import thkoeln.coco.ad.field.Barrier;
import thkoeln.coco.ad.field.FieldRepository;
import thkoeln.coco.ad.field.SquareRepository;
import thkoeln.coco.ad.instruction.*;
import thkoeln.coco.ad.miningMachine.MiningMachineException;
import thkoeln.coco.ad.miningMachine.MiningMachineRepository;
import thkoeln.coco.ad.primitive.Coordinate;
import thkoeln.coco.ad.transport.Connection;
import thkoeln.coco.ad.field.Field;
import thkoeln.coco.ad.transport.ConnectionRepository;
import thkoeln.coco.ad.transport.TransportTechnology;
import thkoeln.coco.ad.miningMachine.MiningMachine;
import thkoeln.coco.ad.transport.TransportTechnologyRepository;

import javax.transaction.Transactional;
import java.util.List;
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

        Connection connection = new Connection(sourceField, sourcePointString, destinationField, destinationPointString);
        connectionRepository.save(connection);

        technology.addConnection(connection);
        transportRepository.save(technology);
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

            boolean successfulMove = machine.executeMoveInstruction(instruction);

            machineRepository.save(machine);
            fieldRepository.save(machine.getCurrentField());

            return successfulMove;
        }
        //TODO: find nicer way of doing this, maybe put some of it into miningMachine, dunno
        if (InstructionFactory.getInstruction(taskString) instanceof TransportInstruction) {
            TransportInstruction instruction = InstructionFactory.getInstruction(taskString);
            Field destinationField = fieldRepository.findById(instruction.getTargetedFieldId()).orElseThrow(() -> new MiningMachineException("Nonexistent FieldID provided: " + instruction.getTargetedFieldId()));

            List<TransportTechnology> transportTechnologies = transportRepository.findAll();

            boolean hasConnection = false;
            Coordinate destinationCoordinate = null;
            for (TransportTechnology technology : transportTechnologies) {
                hasConnection = technology.hasExistingConnection(machine.getCurrentField(), machine.getCurrentPosition(), destinationField);
                if (hasConnection) {
                    destinationCoordinate = technology.getDestinationCoordinate(machine.getCurrentField(), machine.getCurrentPosition(), destinationField);
                    break;
                }
            }

            if (hasConnection && destinationCoordinate != null){
                Field previousField = machine.executeTransportInstruction(instruction, destinationField, destinationCoordinate);

                if (previousField.getId() != machine.getCurrentField().getId()) {
                    fieldRepository.save(machine.getCurrentField());
                    fieldRepository.save(previousField);
                    return true;
                }
            }
            return false;
        }
        if (InstructionFactory.getInstruction(taskString) instanceof EntryInstruction) {
            EntryInstruction instruction = InstructionFactory.getInstruction(taskString);
            Field entryField = fieldRepository.findById(instruction.getTargetedFieldId()).orElseThrow(() -> new MiningMachineException("Nonexistent FieldID provided: " + instruction.getTargetedFieldId()));

            boolean successfulEntry = machine.executeEntryInstruction(entryField);

            machineRepository.save(machine);
            if (machine.getCurrentField() != null) {
                fieldRepository.save(machine.getCurrentField());
            }

            return successfulEntry;
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
        return machine.getCurrentPosition().toString();
    }

    public MiningMachine getMiningMachineById(UUID id) {
        MiningMachine machine = machineRepository.findById(id).orElseThrow(() -> new MiningMachineException("Nonexisting MiningMachineID provided: " + id));
        return machine;
    }

    public UUID addMiningMachine(MiningMachine miningMachine) {
        if (!machineRepository.findById(miningMachine.getId()).isPresent()){
            machineRepository.save(miningMachine);
        }
        return miningMachine.getId();
    }

    @Transactional
    public Field createOrGetInitialField(){
        List<Field> fields = fieldRepository.findAll();
        if (fields.isEmpty()){
            Field field = new Field(5,5);
            squareRepository.saveAll(field.getSquares());
            fieldRepository.save(field);
            return field;
        }
        return fields.get(0);
    }
}

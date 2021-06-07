package thkoeln.coco.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.coco.ad.field.Connection;
import thkoeln.coco.ad.field.Field;
import thkoeln.coco.ad.field.TransportTechnology;
import thkoeln.coco.ad.instruction.BarrierInstruction;
import thkoeln.coco.ad.miningMachine.MiningMachine;
import thkoeln.coco.ad.miningMachine.MiningMachineException;
import thkoeln.coco.ad.parser.InputParser;
import thkoeln.coco.ad.primitives.Barrier;
import thkoeln.coco.ad.instruction.Instruction;
import thkoeln.coco.ad.repository.ConnectionRepository;
import thkoeln.coco.ad.repository.FieldRepository;
import thkoeln.coco.ad.repository.MiningMachineRepository;
import thkoeln.coco.ad.repository.TransportTechnologyRepository;

import java.util.UUID;

@Service
public class CentralControlService {

    private final FieldRepository fieldRepository;
    private final TransportTechnologyRepository technologyRepository;
    private final ConnectionRepository connectionRepository;
    private final MiningMachineRepository miningMachineRepository;

    @Autowired
    public CentralControlService(FieldRepository fieldRepository, TransportTechnologyRepository technologyRepository, ConnectionRepository connectionRepository, MiningMachineRepository miningMachineRepository) {
        this.fieldRepository = fieldRepository;
        this.technologyRepository = technologyRepository;
        this.connectionRepository = connectionRepository;
        this.miningMachineRepository = miningMachineRepository;
    }

    /**
     * This method creates a new field.
     *
     * @param height the height of the field
     * @param width  the width of the field
     * @return the UUID of the created field
     */
    public UUID addField(Integer height, Integer width) {
        try {
            Field field = Field.initializeFromDimensions(height, width);
            fieldRepository.save(field);
            return field.getId();
        } catch (Exception e) {
            throw new MiningMachineException(e.getMessage());
        }
    }

    /**
     * This method adds a barrier to a given field.
     *
     * @param fieldId       the ID of the field the barrier shall be placed on
     * @param barrierString the end points of the barrier
     */
    public void addBarrier(UUID fieldId, String barrierString) {
        InputParser parser = new InputParser();
        if (fieldRepository.findById(fieldId).isPresent()) {
            Field field = fieldRepository.findById(fieldId).get();
            try {
                field.addBarrier(Barrier.createBarrier((BarrierInstruction) parser.parseInput(barrierString)));
            } catch (Exception e) {
                throw new MiningMachineException(e.getMessage());
            }
        }
    }

    /**
     * This method adds a transport technology
     *
     * @param technology the type of the transport technology
     * @return the UUID of the created transport technology
     */
    public UUID addTransportTechnology(String technology) {
        TransportTechnology transportTechnology = TransportTechnology.createTechnology(technology);
        technologyRepository.save(transportTechnology);
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
        if (technologyRepository.existsById(transportTechnologyId) && fieldRepository.existsById(sourceFieldId) && fieldRepository.existsById(destinationFieldId)) {
            Connection connection = Connection.createConnection(transportTechnologyId, sourceFieldId, sourcePointString, destinationFieldId, destinationPointString);
            connectionRepository.save(connection);
            return connection.getId();
        } else throw new MiningMachineException("Tried connecting non-existent Fields or technologies");
    }

    /**
     * This method adds a new mining machine
     *
     * @param name the name of the mining machine
     * @return the UUID of the created mining machine
     */
    public UUID addMiningMachine(String name) {
        MiningMachine miningMachine = new MiningMachine(name);
        miningMachineRepository.save(miningMachine);
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
        InputParser parser = new InputParser();
        Instruction instruction = parser.parseInput(taskString);
        if (miningMachineRepository.findById(miningMachineId).isPresent()) {
            miningMachineRepository.findById(miningMachineId).get().executeCommand(this, instruction);
        } else throw new MiningMachineException("Targeted Mining machine doesn't exist");
        return true;
    }

    /**
     * This method returns the field-ID a mining machine is standing on
     *
     * @param miningMachineId the ID of the mining machine
     * @return the UUID of the field the mining machine is located on
     */
    public UUID getMiningMachineFieldId(UUID miningMachineId) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method returns the point a mining machine is standing on
     *
     * @param miningMachineId the ID of the mining machine
     * @return the point of the mining machine
     */
    public String getMiningMachinePoint(UUID miningMachineId) {
        throw new UnsupportedOperationException();
    }
}

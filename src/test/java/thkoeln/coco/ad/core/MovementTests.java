package thkoeln.coco.ad.core;

import org.springframework.web.context.WebApplicationContext;
import thkoeln.coco.ad.CentralControlService;
import thkoeln.st.springtestlib.core.ObjectInfoRetriever;


import java.lang.reflect.Method;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public abstract class MovementTests {

    protected UUID field1;
    protected UUID field2;
    protected UUID field3;

    protected UUID miningMachine1;
    protected UUID miningMachine2;

    protected UUID transportTechnology1;
    protected UUID transportTechnology2;

    protected UUID connection1;
    protected UUID connection2;
    protected UUID connection3;


    private ObjectInfoRetriever objectInfoRetriever;


    protected MovementTests(WebApplicationContext appContext) {
        objectInfoRetriever = new ObjectInfoRetriever(appContext);
    }


    protected void createWorld(CentralControlService service) {
        field1 = service.addField(6, 6);
        field2 = service.addField(5, 5);
        field3 = service.addField(3, 3);

        miningMachine1 = service.addMiningMachine("marvin");
        miningMachine2 = service.addMiningMachine("darwin");

        service.addBarrier(field1, "(0,3)-(2,3)");
        service.addBarrier(field1, "(3,0)-(3,3)");
        service.addBarrier(field2, "(0,2)-(4,2)");

        transportTechnology1 = service.addTransportTechnology("Tunnel");
        transportTechnology2 = service.addTransportTechnology("Conveyor belt");

        connection1 = service.addConnection(transportTechnology1, field1, "(1,1)", field2, "(0,1)");
        connection2 = service.addConnection(transportTechnology1, field2, "(1,0)", field3, "(2,2)");
        connection3 = service.addConnection(transportTechnology2, field3, "(2,2)", field2, "(1,0)");
    }

    protected void assertPosition(
            CentralControlService service, UUID miningMachineId,
            UUID expectedFieldId, String expectedXY) throws Exception {

        // Assert Grid
        UUID actualFieldId = service.getMiningMachineFieldId(miningMachineId);
        assertEquals(expectedFieldId, actualFieldId);

        // Assert Pos
        String actualPointString = service.getMiningMachinePoint(miningMachineId);
        assertEquals(expectedXY, actualPointString);
    }

    protected void executeTasks(CentralControlService service, UUID miningMachine, String[] tasksArray) {
        for (String tasksString : tasksArray) {
            service.executeCommand(miningMachine, tasksString);
        }
    }
}
